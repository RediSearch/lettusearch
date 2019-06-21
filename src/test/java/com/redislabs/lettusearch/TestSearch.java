package com.redislabs.lettusearch;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.redislabs.lettusearch.search.HighlightOptions;
import com.redislabs.lettusearch.search.HighlightOptions.TagOptions;
import com.redislabs.lettusearch.search.Limit;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResult;
import com.redislabs.lettusearch.search.SearchResults;

import static com.redislabs.lettusearch.Beers.*;

public class TestSearch extends AbstractBaseTest {

	@Test
	public void phoneticFields() {
		SearchResults<String, String> results = commands.search(INDEX, "eldur", SearchOptions.builder().build());
		Assert.assertEquals(7, results.getCount());
	}

	@Test
	public void searchNoContent() {
		SearchResults<String, String> results = commands.search(INDEX, "Hefeweizen", SearchOptions.builder()
				.withScores(true).noContent(true).limit(Limit.builder().num(100).build()).build());
		Assert.assertEquals(22, results.getCount());
		Assert.assertEquals(22, results.size());
		Assert.assertEquals("1836", results.get(0).getDocumentId());
		Assert.assertEquals(1.2, results.get(0).getScore(), 0.000001);
	}

	@Test
	public void get() {
		Map<String, String> map = commands.get(INDEX, "1836");
		Assert.assertEquals("Widmer Brothers Hefeweizen", map.get(FIELD_NAME));
	}

	@Test
	public void del() {
		boolean deleted = commands.del(INDEX, "1836", true);
		Assert.assertTrue(deleted);
		Map<String, String> map = commands.get(INDEX, "1836");
		Assert.assertNull(map);
	}

	@Test
	public void searchReturn() {
		SearchResults<String, String> results = commands.search(INDEX, "pale",
				SearchOptions.builder().returnField(FIELD_NAME).returnField(FIELD_STYLE).build());
		Assert.assertEquals(256, results.getCount());
		SearchResult<String, String> result1 = results.get(0);
		Assert.assertNotNull(result1.get(FIELD_NAME));
		Assert.assertNotNull(result1.get(FIELD_STYLE));
		Assert.assertNull(result1.get(FIELD_ABV));
	}

	@Test
	public void searchHighlight() {
		String term = "pale";
		String query = "@style:" + term;
		TagOptions tagOptions = TagOptions.builder().open("<b>").close("</b>").build();
		SearchResults<String, String> results = commands.search(INDEX, query,
				SearchOptions.builder().highlight(HighlightOptions.builder().build()).build());
		for (SearchResult<String, String> result : results) {
			Assert.assertTrue(highlighted(result, FIELD_STYLE, tagOptions, term));
		}
		results = commands.search(INDEX, query,
				SearchOptions.builder().highlight(HighlightOptions.builder().field(FIELD_NAME).build()).build());
		for (SearchResult<String, String> result : results) {
			Assert.assertFalse(highlighted(result, FIELD_STYLE, tagOptions, term));
		}
		tagOptions = TagOptions.builder().open("[start]").close("[end]").build();
		results = commands.search(INDEX, query, SearchOptions.builder()
				.highlight(HighlightOptions.builder().field(FIELD_STYLE).tags(tagOptions).build()).build());
		for (SearchResult<String, String> result : results) {
			Assert.assertTrue(highlighted(result, FIELD_STYLE, tagOptions, term));
		}
	}

	private boolean highlighted(SearchResult<String, String> result, String fieldName, TagOptions tags, String string) {
		String fieldValue = result.get(fieldName).toLowerCase();
		return fieldValue.contains(tags.getOpen() + string + tags.getClose());
	}

}
