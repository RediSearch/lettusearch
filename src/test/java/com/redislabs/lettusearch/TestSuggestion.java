package com.redislabs.lettusearch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.suggest.SuggestGetOptions;
import com.redislabs.lettusearch.suggest.SuggestResult;

public class TestSuggestion extends AbstractBaseTest {

	@Test
	public void testPhoneticFields() {
		SearchResults<String, String> results = commands.search(Beers.INDEX, "pail");
		assertEquals(256, results.count());
	}

	@Test
	public void testSuggestions() {
		List<SuggestResult<String>> results = commands.sugget(SUGINDEX, "Ame",
				SuggestGetOptions.builder().max(1000l).withScores(true).build());
		assertEquals(8, results.size());
		assertEquals("American Hero", results.get(0).string());
	}

	@Test
	public void testSugdel() {
		Boolean result = commands.sugdel(SUGINDEX, "American Hero");
		assertTrue(result);
		List<SuggestResult<String>> results = commands.sugget(SUGINDEX, "Ame",
				SuggestGetOptions.builder().max(1000l).build());
		assertEquals(7, results.size());
	}

	@Test
	public void testSuglen() {
		long length = commands.suglen(SUGINDEX);
		assertEquals(2245, length);
	}

}
