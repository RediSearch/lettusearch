package com.redislabs.lettusearch;

import static com.redislabs.lettusearch.Beers.ABV;
import static com.redislabs.lettusearch.Beers.INDEX;
import static com.redislabs.lettusearch.Beers.NAME;
import static com.redislabs.lettusearch.Beers.STYLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.redislabs.lettusearch.search.Direction;
import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.search.HighlightOptions;
import com.redislabs.lettusearch.search.Language;
import com.redislabs.lettusearch.search.Limit;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.search.SortBy;
import com.redislabs.lettusearch.search.TagOptions;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class TestSearch extends AbstractBaseTest {

	@Test
	public void phoneticFields() {
		SearchResults<String, String> results = sync.search(INDEX, "eldur");
		assertEquals(7, results.getCount());
	}

	@Test
	public void get() {
		Map<String, String> map = sync.get(INDEX, "1836");
		assertEquals("Widmer Brothers Hefeweizen", map.get(NAME));
	}

	@Test
	public void mget() {
		List<Map<String, String>> mapList = sync.ftMget(INDEX, "1836", "1837", "292929292");
		assertEquals(3, mapList.size());
		assertEquals("Widmer Brothers Hefeweizen", mapList.get(0).get(NAME));
		assertEquals("Hefe Black", mapList.get(1).get(NAME));
		Flux<Map<String, String>> source = connection.reactive().ftMget(INDEX, "1836", "1837");
		StepVerifier.create(source).expectNextMatches(beer -> beer.get(NAME).equals("Widmer Brothers Hefeweizen"))
				.expectNextMatches(beer -> beer.get(NAME).equals("Hefe Black")).expectComplete().verify();
	}

	@Test
	public void del() {
		boolean deleted = sync.del(INDEX, "1836", true);
		assertTrue(deleted);
		Map<String, String> map = sync.get(INDEX, "1836");
		assertTrue(map.isEmpty());
	}

	@Test
	public void noContent() {
		SearchResults<String, String> results = sync.search(INDEX, "Hefeweizen", SearchOptions.<String>builder()
				.withScores(true).noContent(true).limit(Limit.builder().num(100).build()).build());
		assertEquals(22, results.getCount());
		assertEquals(22, results.size());
		assertEquals("1836", results.get(0).getId());
		assertEquals(1.2, results.get(0).getScore(), 0.000001);
	}

	@Test
	public void withPayloads() {
		SearchResults<String, String> results = sync.search(INDEX, "pale",
				SearchOptions.<String>builder().withPayloads(true).build());
		assertEquals(256, results.getCount());
		Document<String, String> result1 = results.get(0);
		assertNotNull(result1.get(NAME));
		assertNotNull(result1.getPayload());
		assertEquals(result1.get(NAME), result1.getPayload());
	}

	@Test
	public void returnField() {
		SearchResults<String, String> results = sync.search(INDEX, "pale",
				SearchOptions.<String>builder().returnField(NAME).returnField(STYLE).build());
		assertEquals(256, results.getCount());
		Document<String, String> result1 = results.get(0);
		assertNotNull(result1.get(NAME));
		assertNotNull(result1.get(STYLE));
		assertNull(result1.get(ABV));
	}

	@Test
	public void allOptions() {
		SearchOptions<String> options = SearchOptions.<String>builder().withPayloads(true).noStopWords(true)
				.limit(Limit.builder().num(100).offset(10).build()).withScores(true)
				.highlight(HighlightOptions.<String>builder().field(NAME)
						.tags(TagOptions.<String>builder().open("<TAG>").close("</TAG>").build()).build())
				.language(Language.English).noContent(false)
				.sortBy(SortBy.<String>builder().direction(Direction.Ascending).field(NAME).build()).verbatim(false)
				.withSortKeys(true).returnField(NAME).returnField(STYLE).build();
		SearchResults<String, String> results = sync.search(INDEX, "pale", options);
		assertEquals(256, results.getCount());
		Document<String, String> result1 = results.get(0);
		assertNotNull(result1.get(NAME));
		assertNotNull(result1.get(STYLE));
		assertNull(result1.get(ABV));
	}

	@Test
	public void invalidReturnField() {
		SearchResults<String, String> results = sync.search(INDEX, "pale",
				SearchOptions.<String>builder().returnField(NAME).returnField(STYLE).returnField("").build());
		assertEquals(256, results.getCount());
		Document<String, String> result1 = results.get(0);
		assertNotNull(result1.get(NAME));
		assertNotNull(result1.get(STYLE));
		assertNull(result1.get(ABV));
	}

	@Test
	public void inKeys() {
		SearchResults<String, String> results = sync.search(INDEX, "*",
				SearchOptions.<String>builder().inKeys(Collections.singletonList("1018")).inKey("2593").build());
		assertEquals(2, results.getCount());
		Document<String, String> result1 = results.get(0);
		assertNotNull(result1.get(NAME));
		assertNotNull(result1.get(STYLE));
		assertEquals("0.07", result1.get(ABV));
	}

	@Test
	public void inFields() {
		SearchResults<String, String> results = sync.search(INDEX, "sculpin",
				SearchOptions.<String>builder().inField(NAME).build());
		assertEquals(2, results.getCount());
		Document<String, String> result1 = results.get(0);
		assertNotNull(result1.get(NAME));
		assertNotNull(result1.get(STYLE));
		assertEquals("0.07", result1.get(ABV));
	}

	@Test
	public void highlight() {
		String term = "pale";
		String query = "@style:" + term;
		TagOptions<String> tagOptions = TagOptions.<String>builder().open("<b>").close("</b>").build();
		SearchResults<String, String> results = sync.search(INDEX, query,
				SearchOptions.<String>builder().highlight(HighlightOptions.<String>builder().build()).build());
		for (Document<String, String> result : results) {
			assertTrue(highlighted(result, STYLE, tagOptions, term));
		}
		results = sync.search(INDEX, query, SearchOptions.<String>builder()
				.highlight(HighlightOptions.<String>builder().field(NAME).build()).build());
		for (Document<String, String> result : results) {
			assertFalse(highlighted(result, STYLE, tagOptions, term));
		}
		tagOptions = TagOptions.<String>builder().open("[start]").close("[end]").build();
		results = sync.search(INDEX, query, SearchOptions.<String>builder()
				.highlight(HighlightOptions.<String>builder().field(STYLE).tags(tagOptions).build()).build());
		for (Document<String, String> result : results) {
			assertTrue(highlighted(result, STYLE, tagOptions, term));
		}
	}

	private boolean highlighted(Document<String, String> result, String fieldName, TagOptions<String> tags,
			String string) {
		String fieldValue = result.get(fieldName).toLowerCase();
		return fieldValue.contains(tags.getOpen() + string + tags.getClose());
	}

	@Test
	public void reactive() {
		SearchResults<String, String> results = connection.reactive()
				.search(INDEX, "pale",
						SearchOptions.<String>builder().limit(Limit.builder().num(100).offset(200).build()).build())
				.block();
		assertEquals(256, results.getCount());
		Document<String, String> result1 = results.get(0);
		assertNotNull(result1.get(NAME));
		assertNotNull(result1.get(STYLE));
		assertNotNull(result1.get(ABV));
	}

	@Test
	public void phonetic() {
		SearchResults<String, String> results = sync.search(Beers.INDEX, "pail");
		assertEquals(256, results.getCount());
	}

	@Test
	public void limit00() {
		SearchResults<String, String> results = sync.search(Beers.INDEX, "*",
				SearchOptions.<String>builder().limit(Limit.builder().num(0).offset(0).build()).build());
		assertEquals(2348, results.getCount());
	}

}
