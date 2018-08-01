package com.redislabs.lettusearch;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.redislabs.lettusearch.index.Document;
import com.redislabs.lettusearch.index.SearchResults;
import com.redislabs.lettusearch.index.SearchResultsNoContent;
import com.redislabs.lettusearch.index.SearchResultsNoContentOutput;
import com.redislabs.lettusearch.index.SearchResultsOutput;
import com.redislabs.lettusearch.index.TextField;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.dynamic.RedisCommandFactory;
import io.lettuce.core.dynamic.output.OutputRegistry;
import io.lettuce.core.dynamic.output.OutputRegistryCommandOutputFactoryResolver;

public class IndexCommandsTest {

	private final static String INDEX = "testIndex";
	private RedisCommandFactory factory;
	private RedisClient client;
	private StatefulRedisConnection<String, String> connection;

	@Before
	public void setup() {
		client = RedisClient.create("redis://localhost:6379");
		connection = client.connect();
		factory = new RedisCommandFactory(connection);
		OutputRegistry outputRegistry = new OutputRegistry();
		outputRegistry.register(SearchResultsOutput.class, SearchResultsOutput::new);
		outputRegistry.register(SearchResultsNoContentOutput.class, SearchResultsNoContentOutput::new);
		factory.setCommandOutputFactoryResolver(new OutputRegistryCommandOutputFactoryResolver(outputRegistry));
		connection.sync().flushall();
	}

	@After
	public void teardown() {
		client.shutdown();
	}

	@Test
	public void testSuggestions() {
		String key = "artists";
		SuggestionCommands indexCommands = factory.getCommands(SuggestionCommands.class);
		indexCommands.add(key, "Herbie Hancock", 1.0);
		indexCommands.add(key, "Herbie Mann", 1.0);
		indexCommands.add(key, "DJ Herbie", 1.0);
		assertEquals(3, (long) indexCommands.length(key));
		indexCommands.delete(key, "DJ Herbie");
		assertEquals(2, (long) indexCommands.length(key));
	}

	@Test
	public void testCreate() {
		IndexCommands commands = factory.getCommands(IndexCommands.class);
		Schema schema = Schema.builder().field(new TextField("field1")).field(new TextField("field2")).build();
		commands.create("testIndex", schema);
		Map<String, Object> doc1 = new LinkedHashMap<>();
		doc1.put("field1", "value1");
		doc1.put("field2", "value2");
		commands.add("testIndex", Document.builder().id("doc1").fields(doc1).build());
	}

	@Test
	public void testSearch() {
		IndexCommands commands = factory.getCommands(IndexCommands.class);
		Schema schema = Schema.builder().field(new TextField("field1")).field(new TextField("field2")).build();
		commands.create("testIndex", schema);
		Map<String, Object> doc1 = new LinkedHashMap<>();
		doc1.put("field1", "this is doc1 value 1");
		doc1.put("field2", "this is doc1 value 2");
		Map<String, Object> doc2 = new LinkedHashMap<>();
		doc2.put("field1", "this is doc2 value 1");
		doc2.put("field2", "this is doc2 value 2");
		commands.add(INDEX, Document.builder().id("doc1").fields(doc1).build());
		commands.add(INDEX, Document.builder().id("doc2").fields(doc2).build());
		SearchResults<String, Object> results = commands.search(INDEX, "value", SearchOptions.builder().build());
		assertEquals(2, results.getCount());
		assertEquals(2, results.getResults().size());
		assertEquals("doc2", results.getResults().get(0).getDocumentId());
		assertEquals("doc1", results.getResults().get(1).getDocumentId());
		assertEquals("this is doc1 value 1", results.getResults().get(1).getFields().get("field1"));
		assertEquals("this is doc2 value 2", results.getResults().get(0).getFields().get("field2"));
		assertEquals(0.2, results.getResults().get(0).getScore(), 0);
		SearchResultsNoContent<String, Object> resultsNoContent = commands.searchNoContent(INDEX, "value",
				SearchOptions.builder().build());
		assertEquals(2, resultsNoContent.getCount());
		assertEquals(2, resultsNoContent.getResults().size());
		assertEquals("doc2", resultsNoContent.getResults().get(0).getDocumentId());
		assertEquals("doc1", resultsNoContent.getResults().get(1).getDocumentId());
		assertEquals(0.2, resultsNoContent.getResults().get(0).getScore(), 0);
	}

}
