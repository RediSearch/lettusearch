package com.redislabs.lettusearch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.suggest.SuggetOptions;
import com.redislabs.lettusearch.suggest.SuggetResult;

public class TestSuggestion extends AbstractBaseTest {

	@Test
	public void testPhoneticFields() {
		SearchResults<String, String> results = commands.search(Beers.INDEX, "pail");
		assertEquals(256, results.getCount());
	}

	@Test
	public void testSuggestions() {
		List<SuggetResult<String>> results = commands.sugget(SUGINDEX, "Ame",
				SuggetOptions.builder().max(1000L).withScores(true).build());
		assertEquals(8, results.size());
		assertEquals("American Hero", results.get(0).getString());
	}

	@Test
	public void testSugdel() {
		Boolean result = commands.sugdel(SUGINDEX, "American Hero");
		assertTrue(result);
		List<SuggetResult<String>> results = commands.sugget(SUGINDEX, "Ame",
				SuggetOptions.builder().max(1000L).build());
		assertEquals(7, results.size());
	}

	@Test
	public void testSuglen() {
		long length = commands.suglen(SUGINDEX);
		assertEquals(2245, length);
	}

}
