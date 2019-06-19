package com.redislabs.lettusearch;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.suggest.SuggestGetOptions;
import com.redislabs.lettusearch.suggest.SuggestResult;
import com.redislabs.lettusearch.suggest.api.sync.SuggestCommands;

public class TestSuggestion extends AbstractBaseTest {

	@Test
	public void testPhoneticFields() {
		SearchResults<String, String> results = connection.sync().search(INDEX, "@style:pail",
				SearchOptions.builder().build());
		Assert.assertEquals(445, results.getCount());
	}

	@Test
	public void testSuggestions() {
		SuggestCommands<String, String> commands = connection.sync();
		List<SuggestResult<String>> results = commands.sugget(SUGINDEX, "Ame",
				SuggestGetOptions.builder().withScores(true).build());
		Assert.assertEquals(5, results.size());
		Assert.assertEquals("American Hero", results.get(0).getString());
	}

}
