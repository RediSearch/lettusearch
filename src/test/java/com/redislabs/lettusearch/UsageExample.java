package com.redislabs.lettusearch;

import java.util.HashMap;
import java.util.Map;

import com.redislabs.lettusearch.search.Schema;
import com.redislabs.lettusearch.search.field.TextField;

public class UsageExample {

	private final static TextField field1 = TextField.builder().name("field1").build();
	private final static Map<String, String> doc1 = new HashMap<>();

	public static void main(String[] args) {
		RediSearchClient client = RediSearchClient.create("redis://localhost");
		StatefulRediSearchConnection<String, String> conn = client.connect();
		RediSearchCommands<String, String> commands = conn.sync();
		commands.create("myIndex", Schema.builder().field(field1).build());
		commands.add("myIndex", "doc1", 1, doc1);
	}
}
