package com.redislabs.lettusearch;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.redislabs.lettusearch.index.Document;
import com.redislabs.lettusearch.index.Schema;
import com.redislabs.lettusearch.index.SearchOptions;
import com.redislabs.lettusearch.index.SearchResults;
import com.redislabs.lettusearch.index.SearchResultsNoContent;
import com.redislabs.lettusearch.index.TextField;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;

public class IndexCommandsTest {

	private final static String INDEX = "testIndex";
	private RedisClient redisClient;
	private RediSearchClient client;

	@Before
	public void setup() {
		redisClient = RedisClient.create("redis://localhost");
		StatefulRedisConnection<String, String> connection = redisClient.connect();
		connection.sync().flushall();
		client = new RediSearchClient(connection);
	}

	@After
	public void teardown() {
		redisClient.shutdown();
	}

	@Test
	public void testSuggestions() {
		String key = "artists";
		RediSearchCommands indexCommands = client.getCommands();
		indexCommands.suggestionAdd(key, "Herbie Hancock", 1.0);
		indexCommands.suggestionAdd(key, "Herbie Mann", 1.0);
		indexCommands.suggestionAdd(key, "DJ Herbie", 1.0);
		assertEquals(3, (long) indexCommands.suggestionLength(key));
		indexCommands.suggestionDelete(key, "DJ Herbie");
		assertEquals(2, (long) indexCommands.suggestionLength(key));
	}

	@Test
	public void testCreate() {
		RediSearchCommands commands = client.getCommands();
		TextField field1 = new TextField();
		field1.setName("field1");
		TextField field2 = new TextField();
		field2.setName("field2");
		Schema schema = Schema.builder().field(field1).field(field2).build();
		commands.create("testIndex", schema);
		Map<String, String> doc1 = new LinkedHashMap<>();
		doc1.put("field1", "value1");
		doc1.put("field2", "value2");
		commands.add("testIndex", Document.builder().id("doc1").fields(doc1).build());
	}

	@Test
	public void testSearch() {
		RediSearchCommands commands = client.getCommands();
		TextField field1 = new TextField();
		field1.setName("field1");
		TextField field2 = new TextField();
		field2.setName("field2");
		Schema schema = Schema.builder().field(field1).field(field2).build();
		commands.create("testIndex", schema);
		Map<String, String> doc1 = new LinkedHashMap<>();
		doc1.put("field1", "this is doc1 value 1");
		doc1.put("field2", "this is doc1 value 2");
		Map<String, String> doc2 = new LinkedHashMap<>();
		doc2.put("field1", "this is doc2 value 1");
		doc2.put("field2", "this is doc2 value 2");
		commands.add(INDEX, Document.builder().id("doc1").fields(doc1).build());
		commands.add(INDEX, Document.builder().id("doc2").fields(doc2).build());
		SearchResults<String, String> results = commands.search(INDEX, "value", SearchOptions.builder().build());
		assertEquals(2, results.getCount());
		assertEquals(2, results.getResults().size());
		assertEquals("doc2", results.getResults().get(0).getDocumentId());
		assertEquals("doc1", results.getResults().get(1).getDocumentId());
		assertEquals("this is doc1 value 1", results.getResults().get(1).getFields().get("field1"));
		assertEquals("this is doc2 value 2", results.getResults().get(0).getFields().get("field2"));
		assertEquals(0.2, results.getResults().get(0).getScore(), 0);
		SearchResultsNoContent<String, String> resultsNoContent = commands.searchNoContent(INDEX, "value",
				SearchOptions.builder().build());
		assertEquals(2, resultsNoContent.getCount());
		assertEquals(2, resultsNoContent.getResults().size());
		assertEquals("doc2", resultsNoContent.getResults().get(0).getDocumentId());
		assertEquals("doc1", resultsNoContent.getResults().get(1).getDocumentId());
		assertEquals(0.2, resultsNoContent.getResults().get(0).getScore(), 0);
	}

}
