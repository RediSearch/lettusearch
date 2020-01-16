package com.redislabs.lettusearch;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.suggest.SuggestGetOptions;
import com.redislabs.lettusearch.suggest.SuggestResult;

public class TestSuggestion extends AbstractBaseTest {

	@Test
	public void testPhoneticFields() {
		SearchResults<String, String> results = commands.search(Beers.INDEX, "pail");
		Assert.assertEquals(256, results.getCount());
	}

	@Test
	public void testSuggestions() {
		List<SuggestResult<String>> results = commands.sugget(SUGINDEX, "Ame",
				SuggestGetOptions.builder().max(1000l).withScores(true).build());
		Assert.assertEquals(8, results.size());
		Assert.assertEquals("American Hero", results.get(0).getString());
	}

	@Test
	public void testSugdel() {
		Boolean result = commands.sugdel(SUGINDEX, "American Hero");
		Assert.assertTrue(result);
		List<SuggestResult<String>> results = commands.sugget(SUGINDEX, "Ame",
				SuggestGetOptions.builder().max(1000l).build());
		Assert.assertEquals(7, results.size());
	}

	@Test
	public void testSuglen() {
		long length = commands.suglen(SUGINDEX);
		Assert.assertEquals(2245, length);
	}

}
