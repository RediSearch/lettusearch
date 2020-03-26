package com.redislabs.lettusearch;

import static com.redislabs.lettusearch.Beers.FIELD_ID;
import static com.redislabs.lettusearch.Beers.FIELD_NAME;
import static com.redislabs.lettusearch.Beers.INDEX;
import static com.redislabs.lettusearch.Beers.load;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractBaseTest {

	protected final static String SUGINDEX = "beersSug";

	private RediSearchClient client;
	protected StatefulRediSearchConnection<String, String> connection;
	protected List<Map<String, String>> beers;
	protected RediSearchCommands<String, String> commands;

	@BeforeEach
	public void setup() throws IOException {
		beers = load();
		client = RediSearchClient.create("redis://localhost:6379");
		connection = client.connect();
		commands = connection.sync();
		commands.flushall();
		commands.create(INDEX, UsageExample.SCHEMA);
		for (Map<String, String> beer : beers) {
			commands.add(INDEX, beer.get(FIELD_ID), 1, beer, null, null);
			commands.sugadd(SUGINDEX, beer.get(FIELD_NAME), 1, false, null);
		}
	}

	@AfterEach
	public void teardown() {
		if (connection != null) {
			connection.close();
		}
		if (client != null) {
			client.shutdown();
		}
	}

}
