package com.redislabs.lettusearch;

import static com.redislabs.lettusearch.Beers.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;

public abstract class AbstractBaseTest {

	protected final static String SUGINDEX = "beersSug";

	private RediSearchClient client;
	protected StatefulRediSearchConnection<String, String> connection;
	protected List<Map<String, String>> beers;
	protected RediSearchCommands<String, String> commands;

	@Before
	public void setup() throws IOException {
		this.beers = load();
		client = RediSearchClient.create("redis://localhost");
		connection = client.connect();
		commands = connection.sync();
		commands.flushall();
		commands.create(INDEX, UsageExample.SCHEMA);
		for (Map<String, String> beer : beers) {
			commands.add(INDEX, beer.get(FIELD_ID), 1, beer);
			commands.sugadd(SUGINDEX, beer.get(FIELD_NAME), 1);
		}
	}

	@After
	public void teardown() {
		if (connection != null) {
			connection.close();
		}
		if (client != null) {
			client.shutdown();
		}
	}

}
