package com.redislabs.lettusearch;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.redislabs.lettusearch.index.Document;
import com.redislabs.lettusearch.index.Schema;
import com.redislabs.lettusearch.index.TextField;

import io.lettuce.core.RedisClient;
import io.lettuce.core.dynamic.RedisCommandFactory;

public class IndexCommandsTest {

	@Test
	public void testSuggestions() {
		String key = "artists";
		RedisClient client = RedisClient.create("redis://localhost:6379");
		RedisCommandFactory factory = new RedisCommandFactory(client.connect());
		SuggestionCommands indexCommands = factory.getCommands(SuggestionCommands.class);
		indexCommands.add(key, "Herbie Hancock", 1.0);
		indexCommands.add(key, "Herbie Mann", 1.0);
		indexCommands.add(key, "DJ Herbie", 1.0);
		List<String> suggestions = indexCommands.getFuzzy(key, "Hor");
		for (String suggestion : suggestions) {
			System.out.println(suggestion);
		}
		assertEquals(3, (long) indexCommands.length(key));
		indexCommands.delete(key, "DJ Herbie");
		assertEquals(2, (long) indexCommands.length(key));
		client.shutdown();
	}

	@Test
	public void testCreate() {
		RedisClient client = RedisClient.create("redis://localhost:6379");
		RedisCommandFactory factory = new RedisCommandFactory(client.connect());
		IndexCommands commands = factory.getCommands(IndexCommands.class);
		Schema schema = Schema.builder().field(new TextField("field1")).field(new TextField("field2")).build();
		commands.create("testIndex", schema);
		Map<String, Object> doc1 = new LinkedHashMap<>();
		doc1.put("field1", "value1");
		doc1.put("field2", "value2");
		commands.add("testIndex", Document.builder().id("doc1").fields(doc1).build());
		client.shutdown();
	}

}
