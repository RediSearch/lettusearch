package com.redislabs.lettusearch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.search.Schema;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.search.api.sync.SearchCommands;
import com.redislabs.lettusearch.suggest.GetOptions;
import com.redislabs.lettusearch.suggest.SuggestResult;
import com.redislabs.lettusearch.suggest.Suggestion;
import com.redislabs.lettusearch.suggest.api.sync.SuggestCommands;

public class IndexCommandsTest {

	private final static String INDEX = "testIndex";
	private RediSearchClient client;

	@Before
	public void setup() {
		client = RediSearchClient.create("redis://localhost");
		client.connect().sync().flushall();
	}

	@After
	public void teardown() {
		client.shutdown();
	}

	@Test
	public void testAdd() {
		StatefulRediSearchConnection<String, String> connection = client.connect();
		SearchCommands<String, String> commands = connection.sync();
		connection.sync().create("testIndex",
				Schema.builder().textField("field1", false).textField("field2", true).build());
		Map<String, String> fields1 = new HashMap<>();
		fields1.put("field1", "this is doc 1 value 1");
		fields1.put("field2", "this is doc 1 value 2");
		Map<String, String> fields2 = new HashMap<>();
		fields2.put("field1", "this is doc 2 value 1");
		fields2.put("field2", "this is doc 2 value 2");
		commands.add(INDEX, Document.builder().id(String.valueOf("doc1")).score(1).fields(fields1).build());
		commands.add(INDEX, Document.builder().id(String.valueOf("doc2")).score(1).fields(fields2).build());
		connection.close();
	}

	@Test
	public void testSuggestions() {
		String key = "artists";
		StatefulRediSearchConnection<String, String> connection = client.connect();
		SuggestCommands<String, String> commands = connection.sync();
		String hancock = "Herbie Hancock";
		String mann = "Herbie Mann";
		commands.add(key, Suggestion.builder().string(hancock).build());
		commands.add(key, Suggestion.builder().string(mann).build());
		commands.add(key, Suggestion.builder().string("DJ Herbie").build());
		List<SuggestResult<String>> results = commands.get(key, "Herb", GetOptions.builder().build());
		Assert.assertEquals(2, results.size());
		Assert.assertTrue(results.stream().anyMatch(result -> hancock.equals(result.getString())));
		Assert.assertTrue(results.stream().anyMatch(result -> mann.equals(result.getString())));
	}

	@Test
	public void testSearch() {
		testAdd();
		StatefulRediSearchConnection<String, String> connection = client.connect();
		SearchResults<String, String> results = connection.sync().search(INDEX, "value",
				SearchOptions.builder().withScores(true).build());
		Assert.assertEquals(2, results.getCount());
		Assert.assertEquals(2, results.getResults().size());
		Assert.assertEquals("doc2", results.getResults().get(0).getDocumentId());
		Assert.assertEquals("doc1", results.getResults().get(1).getDocumentId());
		Assert.assertEquals("this is doc 1 value 1", results.getResults().get(1).getFields().get("field1"));
		Assert.assertEquals("this is doc 2 value 2", results.getResults().get(0).getFields().get("field2"));
		Assert.assertEquals(0.1333333, results.getResults().get(0).getScore(), 0.000001);
		connection.close();
	}

	@Test
	public void testSearchNoContent() {
		testAdd();
		StatefulRediSearchConnection<String, String> connection = client.connect();
		SearchResults<String, String> results = connection.sync().search(INDEX, "value",
				SearchOptions.builder().withScores(true).noContent(true).build());
		Assert.assertEquals(2, results.getCount());
		Assert.assertEquals(2, results.getResults().size());
		Assert.assertEquals("doc2", results.getResults().get(0).getDocumentId());
		Assert.assertEquals("doc1", results.getResults().get(1).getDocumentId());
		Assert.assertEquals(0.1333333, results.getResults().get(0).getScore(), 0.000001);
	}

}
