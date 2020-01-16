package com.redislabs.lettusearch;

import static com.redislabs.lettusearch.Beers.FIELD_ID;
import static com.redislabs.lettusearch.Beers.FIELD_NAME;
import static com.redislabs.lettusearch.Beers.INDEX;
import static com.redislabs.lettusearch.Beers.load;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import redis.embedded.RedisExecProvider;
import redis.embedded.RedisServer;
import redis.embedded.util.OS;

public abstract class AbstractBaseTest {

	protected final static String SUGINDEX = "beersSug";

	private static RedisServer server;

	private RediSearchClient client;
	protected StatefulRediSearchConnection<String, String> connection;
	protected List<Map<String, String>> beers;
	protected RediSearchCommands<String, String> commands;

	@BeforeClass
	public static void setupRedis() throws IOException {
		RedisExecProvider provider = RedisExecProvider.defaultProvider().override(OS.MAC_OS_X,
				"/usr/local/bin/redis-server");
		server = RedisServer.builder().redisExecProvider(provider)
				.setting("loadmodule /Users/jruaux/git/RediSearch/build/redisearch.so").build();
		server.start();
	}

	@Before
	public void setup() throws IOException {
		this.beers = load();
		client = RediSearchClient.create("redis://localhost");
		connection = client.connect();
		commands = connection.sync();
		connection.sync().flushall();
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

	@AfterClass
	public static void teardownRedis() {
		if (server != null) {
			server.stop();
		}
	}

}
