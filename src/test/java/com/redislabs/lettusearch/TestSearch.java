package com.redislabs.lettusearch;

import static com.redislabs.lettusearch.Beers.FIELD_ABV;
import static com.redislabs.lettusearch.Beers.FIELD_NAME;
import static com.redislabs.lettusearch.Beers.FIELD_STYLE;
import static com.redislabs.lettusearch.Beers.INDEX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.search.HighlightOptions;
import com.redislabs.lettusearch.search.Limit;
import com.redislabs.lettusearch.search.SearchArgs;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.search.TagOptions;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class TestSearch extends AbstractBaseTest {

	@Test
	public void phoneticFields() {
		SearchResults<String, String> results = commands.search(INDEX, SearchArgs.builder().query("eldur").build());
		assertEquals(7, results.getCount());
	}

	@Test
	public void searchNoContent() {
		SearchResults<String, String> results = commands.search(INDEX,
				SearchArgs.builder().query("Hefeweizen").options(SearchOptions.builder().withScores(true)
						.noContent(true).limit(Limit.builder().num(100).build()).build()).build());
		assertEquals(22, results.getCount());
		assertEquals(22, results.size());
		assertEquals("1836", results.get(0).getId());
		assertEquals(1.2, results.get(0).getScore(), 0.000001);
	}

	@Test
	public void get() {
		Map<String, String> map = commands.get(INDEX, "1836");
		assertEquals("Widmer Brothers Hefeweizen", map.get(FIELD_NAME));
	}

	@Test
	public void mget() throws InterruptedException {
		List<Map<String, String>> mapList = commands.ftMget(INDEX, "1836", "1837", "292929292");
		assertEquals(3, mapList.size());
		assertEquals("Widmer Brothers Hefeweizen", mapList.get(0).get(FIELD_NAME));
		assertEquals("Hefe Black", mapList.get(1).get(FIELD_NAME));
		Flux<Map<String, String>> source = connection.reactive().ftMget(INDEX, "1836", "1837");
		StepVerifier.create(source).expectNextMatches(beer -> beer.get(FIELD_NAME).equals("Widmer Brothers Hefeweizen"))
				.expectNextMatches(beer -> beer.get(FIELD_NAME).equals("Hefe Black")).expectComplete().verify();
	}

	@Test
	public void del() {
		boolean deleted = commands.del(INDEX, "1836", true);
		assertTrue(deleted);
		Map<String, String> map = commands.get(INDEX, "1836");
		assertNull(map);
	}

	@Test
	public void searchReturn() {
		SearchResults<String, String> results = commands.search(INDEX, SearchArgs.builder().query("pale")
				.options(SearchOptions.builder().returnField(FIELD_NAME).returnField(FIELD_STYLE).build()).build());
		assertEquals(256, results.getCount());
		Document<String, String> result1 = results.get(0);
		assertNotNull(result1.get(FIELD_NAME));
		assertNotNull(result1.get(FIELD_STYLE));
		assertNull(result1.get(FIELD_ABV));
	}

	@Test
	public void searchInvalidReturn() {
		SearchResults<String, String> results = commands.search(INDEX, SearchArgs.builder().query("pale").options(
				SearchOptions.builder().returnField(FIELD_NAME).returnField(FIELD_STYLE).returnField("").build())
				.build());
		assertEquals(256, results.getCount());
		Document<String, String> result1 = results.get(0);
		assertNotNull(result1.get(FIELD_NAME));
		assertNotNull(result1.get(FIELD_STYLE));
		assertNull(result1.get(FIELD_ABV));
	}

	@Test
	public void searchHighlight() {
		String term = "pale";
		String query = "@style:" + term;
		TagOptions tagOptions = TagOptions.builder().open("<b>").close("</b>").build();
		SearchResults<String, String> results = commands.search(INDEX, SearchArgs.builder().query(query)
				.options(SearchOptions.builder().highlight(HighlightOptions.builder().build()).build()).build());
		for (Document<String, String> result : results) {
			assertTrue(highlighted(result, FIELD_STYLE, tagOptions, term));
		}
		results = commands.search(INDEX,
				SearchArgs.builder().query(query).options(
						SearchOptions.builder().highlight(HighlightOptions.builder().field(FIELD_NAME).build()).build())
						.build());
		for (Document<String, String> result : results) {
			assertFalse(highlighted(result, FIELD_STYLE, tagOptions, term));
		}
		tagOptions = TagOptions.builder().open("[start]").close("[end]").build();
		results = commands.search(INDEX,
				SearchArgs.builder().query(query).options(SearchOptions.builder()
						.highlight(HighlightOptions.builder().field(FIELD_STYLE).tags(tagOptions).build()).build())
						.build());
		for (Document<String, String> result : results) {
			assertTrue(highlighted(result, FIELD_STYLE, tagOptions, term));
		}
	}

	private boolean highlighted(Document<String, String> result, String fieldName, TagOptions tags, String string) {
		String fieldValue = result.get(fieldName).toLowerCase();
		return fieldValue.contains(tags.getOpen() + string + tags.getClose());
	}

}
