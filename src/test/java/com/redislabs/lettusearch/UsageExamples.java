package com.redislabs.lettusearch;

import static com.redislabs.lettusearch.Beers.ID;
import static com.redislabs.lettusearch.Beers.NAME;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.Test;

import com.redislabs.lettusearch.index.Schema;
import com.redislabs.lettusearch.index.field.TextField;
import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.search.SearchResults;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.support.ConnectionPoolSupport;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UsageExamples extends AbstractBaseTest {

	@Test
	public void basic() {
		// tag::basic[]
		RediSearchClient client = RediSearchClient.create(RedisURI.create(host, port)); // <1>
		StatefulRediSearchConnection<String, String> connection = client.connect(); // <2>
		RediSearchCommands<String, String> commands = connection.sync(); // <3>
		commands.create("idx", Schema.<String>builder().field(TextField.<String>builder().name(NAME).build()).build()); // <4>
		commands.add("idx", Document.<String, String>builder().id(ID).score(1D).field(NAME, "La Chouffe").build()); // <5>
		SearchResults<String, String> results = commands.search("idx", "chou*"); // <6>
		results.forEach(System.out::println);
		// end::basic[]
	}

	@Test
	public void pipelining() {
		List<Document<String, String>> docs = Arrays
				.asList(Document.<String, String>builder().id("doc1").field(NAME, "somevalue").build());
		connection.sync().create("idx",
				Schema.<String>builder().field(TextField.<String>builder().name(NAME).build()).build());
		// tag::pipelining[]
		RediSearchClient client = RediSearchClient.create(RedisURI.create(host, port)); // <1>
		StatefulRediSearchConnection<String, String> connection = client.connect(); // <2>
		RediSearchAsyncCommands<String, String> commands = connection.async(); // <3>
		commands.setAutoFlushCommands(false); // <4>
		List<RedisFuture<?>> futures = new ArrayList<>();
		for (Document<String, String> doc : docs) { // <5>
			RedisFuture<?> future = commands.add("idx", doc); // <6>
			futures.add(future);
		}
		commands.flushCommands(); // <7>
		for (RedisFuture<?> future : futures) {
			try {
				future.get(1, TimeUnit.SECONDS); // <8>
			} catch (InterruptedException e) {
				log.debug("Command interrupted", e);
			} catch (ExecutionException e) {
				log.error("Could not execute command", e);
			} catch (TimeoutException e) {
				log.error("Command timed out", e);
			}
		}
		// end::pipelining[]
	}

	@Test
	public void connectionPooling() throws Exception {
		connection.sync().create("idx",
				Schema.<String>builder().field(TextField.<String>builder().name(NAME).build()).build()); // <4>
		// tag::connectionPooling[]
		RediSearchClient client = RediSearchClient.create(RedisURI.create(host, port)); // <1>
		GenericObjectPoolConfig<StatefulRediSearchConnection<String, String>> config = new GenericObjectPoolConfig<>(); // <2>
		config.setMaxTotal(8);
		GenericObjectPool<StatefulRediSearchConnection<String, String>> pool = ConnectionPoolSupport
				.createGenericObjectPool(client::connect, config); // <3>
		// The connection pool can now be passed to worker threads
		try (StatefulRediSearchConnection<String, String> connection = pool.borrowObject()) { // <4>
			RediSearchCommands<String, String> commands = connection.sync(); // <5>
			commands.search("idx", "*"); // <6>
		}
		// end::connectionPooling[]
		pool.close();
	}
}
