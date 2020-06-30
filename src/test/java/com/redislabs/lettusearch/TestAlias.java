package com.redislabs.lettusearch;

import com.redislabs.lettusearch.search.SearchResults;
import io.lettuce.core.RedisCommandExecutionException;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static com.redislabs.lettusearch.Beers.INDEX;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class TestAlias extends AbstractBaseTest {

	private final static String ALIAS = "alias123";

	@Test
	public void syncAdd() {
		sync.aliasAdd(ALIAS, INDEX);
		SearchResults<String, String> results = sync.search(ALIAS, "*");
		assertTrue(results.size() > 0);
	}

	@Test
	public void asyncAdd() throws ExecutionException, InterruptedException {
		async.aliasAdd(ALIAS, INDEX).get();
		SearchResults<String, String> results = async.search(ALIAS, "*").get();
		assertTrue(results.size() > 0);
	}

	@Test
	public void reactiveAdd() {
		reactive.aliasAdd(ALIAS, INDEX).block();
		SearchResults<String, String> results = reactive.search(ALIAS, "*").block();
		assertTrue(results.size() > 0);
	}

	@Test
	public void syncDel() {
		syncAdd();
		sync.aliasDel(ALIAS);
		try {
			sync.search(ALIAS, "*");
			fail("Alias was not removed");
		} catch (RedisCommandExecutionException e) {
			assertTrue(e.getMessage().contains("no such index") || e.getMessage().contains("Unknown Index name"));
		}
	}

	@Test
	public void asyncDel() throws ExecutionException, InterruptedException {
		asyncAdd();
		async.aliasDel(ALIAS).get();
		try {
			async.search(ALIAS, "*").get();
			fail("Alias was not removed");
		} catch (ExecutionException e) {
			assertTrue(e.getCause().getMessage().contains("no such index") || e.getCause().getMessage().contains("Unknown Index name"));
		}
	}

	@Test
	public void reactiveDel() {
		reactiveAdd();
		reactive.aliasDel(ALIAS).block();
		try {
			reactive.search(ALIAS, "*").block();
			fail("Alias was not removed");
		} catch (RedisCommandExecutionException e) {
			assertTrue(e.getMessage().contains("no such index") || e.getMessage().contains("Unknown Index name"));
		}
	}

	@Test
	public void syncUpdate() {
		syncAdd();
		String newAlias = "alias456";
		sync.aliasUpdate(newAlias, INDEX);
		assertTrue(sync.search(newAlias, "*").size() > 0);
	}

	@Test
	public void asyncUpdate() throws ExecutionException, InterruptedException {
		asyncAdd();
		String newAlias = "alias456";
		async.aliasUpdate(newAlias, INDEX);
		assertTrue(async.search(newAlias, "*").get().size() > 0);
	}

	@Test
	public void reactiveUpdate() {
		reactiveAdd();
		String newAlias = "alias456";
		reactive.aliasUpdate(newAlias, INDEX).block();
		assertTrue(reactive.search(newAlias, "*").block().size() > 0);
	}

}
