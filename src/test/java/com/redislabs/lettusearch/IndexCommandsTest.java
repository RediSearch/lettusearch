package com.redislabs.lettusearch;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.redislabs.lettusearch.api.Document;
import com.redislabs.lettusearch.api.Schema;
import com.redislabs.lettusearch.api.async.SearchAsyncCommands;

public class IndexCommandsTest {

	private final static String INDEX = "testIndex";
	private static final int BATCH_SIZE = 50;
	private RediSearchClient client;
	private final static UUID JVM_UUID = UUID.randomUUID();

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
		StatefulSearchConnection<String, String> connection = client.connect();
		SearchAsyncCommands<String, String> commands = connection.async();
		connection.sync().create("testIndex", Schema.builder().textField("field1", false).textField("field2", true).build());
		commands.setAutoFlushCommands(false);
		Map<String, String> fields = new HashMap<>();
		fields.put("field1", "value1");
		fields.put("field2", "value2");
		int batchIndex = 0;
		for (int batch = 0; batch < 10; batch++) {
			long start = System.nanoTime();
			for (int index = 0; index < BATCH_SIZE; index++) {
				long localId = batchIndex * 1000 + index;
				String docId = JVM_UUID + "-" + localId;
				commands.add(INDEX, Document.builder().id(docId).score(1).fields(fields).build());
			}
			commands.flushCommands();
			long end = System.nanoTime();
			long duration = (end - start) / 1000;
			System.out.println("Duration: " + duration);
			batchIndex++;
		}
		connection.close();
	}
//
//	@Test
//	public void testSuggestions() {
//		String key = "artists";
//		SearchApiCommands indexCommands = client.getCommands();
//		indexCommands.suggestionAdd(key, "Herbie Hancock", 1.0);
//		indexCommands.suggestionAdd(key, "Herbie Mann", 1.0);
//		indexCommands.suggestionAdd(key, "DJ Herbie", 1.0);
//		assertEquals(3, (long) indexCommands.suggestionLength(key));
//		indexCommands.suggestionDelete(key, "DJ Herbie");
//		assertEquals(2, (long) indexCommands.suggestionLength(key));
//	}

//	@Test
//	public void testCreate() {
//		SearchApiCommands commands = client.getCommands();
//		TextField field1 = new TextField();
//		field1.setName("field1");
//		TextField field2 = new TextField();
//		field2.setName("field2");
//		Schema schema = Schema.builder().field(field1).field(field2).build();
//		commands.create("testIndex", schema);
//		Map<String, String> doc1 = new LinkedHashMap<>();
//		doc1.put("field1", "value1");
//		doc1.put("field2", "value2");
//		commands.add("testIndex", Document.builder().id("doc1").fields(doc1).build());
//	}
//
//	@Test
//	public void testSearch() {
//		SearchApiCommands commands = client.getCommands();
//		TextField field1 = new TextField();
//		field1.setName("field1");
//		TextField field2 = new TextField();
//		field2.setName("field2");
//		Schema schema = Schema.builder().field(field1).field(field2).build();
//		commands.create("testIndex", schema);
//		Map<String, String> doc1 = new LinkedHashMap<>();
//		doc1.put("field1", "this is doc1 value 1");
//		doc1.put("field2", "this is doc1 value 2");
//		Map<String, String> doc2 = new LinkedHashMap<>();
//		doc2.put("field1", "this is doc2 value 1");
//		doc2.put("field2", "this is doc2 value 2");
//		commands.add(INDEX, Document.builder().id("doc1").fields(doc1).build());
//		commands.add(INDEX, Document.builder().id("doc2").fields(doc2).build());
//		SearchResults<String, String> results = commands.search(INDEX, "value", SearchOptions.builder().build());
//		assertEquals(2, results.getCount());
//		assertEquals(2, results.getResults().size());
//		assertEquals("doc2", results.getResults().get(0).getDocumentId());
//		assertEquals("doc1", results.getResults().get(1).getDocumentId());
//		assertEquals("this is doc1 value 1", results.getResults().get(1).getFields().get("field1"));
//		assertEquals("this is doc2 value 2", results.getResults().get(0).getFields().get("field2"));
//		assertEquals(0.2, results.getResults().get(0).getScore(), 0);
//		SearchResultsNoContent<String, String> resultsNoContent = commands.searchNoContent(INDEX, "value",
//				SearchOptions.builder().build());
//		assertEquals(2, resultsNoContent.getCount());
//		assertEquals(2, resultsNoContent.getResults().size());
//		assertEquals("doc2", resultsNoContent.getResults().get(0).getDocumentId());
//		assertEquals("doc1", resultsNoContent.getResults().get(1).getDocumentId());
//		assertEquals(0.2, resultsNoContent.getResults().get(0).getScore(), 0);
//	}

}
