package com.redislabs.lettusearch;

import org.junit.Assert;
import org.junit.Test;

import com.redislabs.lettusearch.search.Limit;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;

public class SearchTest extends BaseTest {

	@Test
	public void testPhoneticFields() {
		SearchResults<String, String> results = connection.sync().search(INDEX, "@style:pail",
				SearchOptions.builder().build());
		Assert.assertEquals(445, results.getCount());
	}

//	@Test
//	public void testSuggestions() {
//		String key = "artists";
//		StatefulRediSearchConnection<String, String> connection = client.connect();
//		SuggestCommands<String, String> commands = connection.sync();
//		String hancock = "Herbie Hancock";
//		String mann = "Herbie Mann";
//		commands.sugadd(key, hancock, 1, SuggestAddOptions.builder().build());
//		commands.sugadd(key, mann, 1, SuggestAddOptions.builder().build());
//		commands.sugadd(key, "DJ Herbie", 1, SuggestAddOptions.builder().build());
//		List<SuggestResult<String>> results = commands.sugget(key, "Herb",
//				SuggestGetOptions.builder().withScores(true).withPayloads(true).build());
//		Assert.assertEquals(2, results.size());
//		Assert.assertTrue(results.stream().anyMatch(result -> hancock.equals(result.getString())));
//		Assert.assertTrue(results.stream().anyMatch(result -> mann.equals(result.getString())));
//	}
//
//
	@Test
	public void testSearchNoContent() {
		SearchResults<String, String> results = connection.sync().search(INDEX, "Hefeweizen", SearchOptions.builder()
				.withScores(true).noContent(true).limit(Limit.builder().num(100).build()).build());
		Assert.assertEquals(42, results.getCount());
		Assert.assertEquals(42, results.getResults().size());
		Assert.assertEquals("1836", results.getResults().get(0).getDocumentId());
		Assert.assertEquals(7.5, results.getResults().get(0).getScore(), 0.000001);
	}

}
