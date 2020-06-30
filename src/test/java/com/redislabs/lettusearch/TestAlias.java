package com.redislabs.lettusearch;

import static com.redislabs.lettusearch.Beers.INDEX;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.redislabs.lettusearch.search.SearchResults;

import io.lettuce.core.RedisCommandExecutionException;

public class TestAlias extends AbstractBaseTest {

	private final static String ALIAS = "alias123";

	@Test
	public void add() {
		commands.aliasAdd(ALIAS, INDEX);
		SearchResults<String, String> results = commands.search(ALIAS, "*");
		assertTrue(results.size() > 0);
	}

	@Test
	public void del() {
		add();
		commands.aliasDel(ALIAS);
		try {
			commands.search(ALIAS, "*");
			fail("Alias was not removed");
		} catch (RedisCommandExecutionException e) {
			assertTrue(e.getMessage().contains("no such index") || e.getMessage().contains("Unknown Index name"));
		}
	}

	@Test
	public void update() {
		add();
		String newAlias = "alias456";
		commands.aliasUpdate(newAlias, INDEX);
		assertTrue(commands.search(newAlias, "*").size() > 0);
	}

}
