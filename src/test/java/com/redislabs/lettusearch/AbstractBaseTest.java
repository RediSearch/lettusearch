package com.redislabs.lettusearch;

import static com.redislabs.lettusearch.Beers.NAME;
import static com.redislabs.lettusearch.Beers.INDEX;
import static com.redislabs.lettusearch.Beers.load;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.suggest.Suggestion;

public abstract class AbstractBaseTest {

	protected final static String SUGINDEX = "beersSug";

	private RediSearchClient client;
	protected StatefulRediSearchConnection<String, String> connection;
	protected List<Document<String, String>> beers;
	protected RediSearchCommands<String, String> commands;

	@BeforeEach
	public void setup() throws IOException {
		beers = load();
		client = RediSearchClient.create("redis://localhost:6379");
		connection = client.connect();
		commands = connection.sync();
		commands.flushall();
		commands.create(INDEX, UsageExample.SCHEMA, null);
		for (Document<String, String> beer : beers) {
			commands.add(INDEX, beer, null);
			commands.sugadd(SUGINDEX, Suggestion.<String>builder().string(beer.get(NAME)).score(1d).build(),
					false);
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
