package com.redislabs.lettusearch;

import static com.redislabs.lettusearch.Beers.INDEX;

import org.junit.Assert;
import org.junit.Test;

import com.redislabs.lettusearch.search.SearchResults;

import io.lettuce.core.RedisCommandExecutionException;

public class TestAlias extends AbstractBaseTest {

	private final static String ALIAS = "alias123";

	@Test
	public void testAddAlias() {
		commands.aliasAdd(ALIAS, INDEX);
		SearchResults<String, String> results = commands.search(ALIAS, "*");
		Assert.assertTrue(results.size() > 0);
	}

	@Test
	public void testDelAlias() {
		testAddAlias();
		commands.aliasDel(ALIAS);
		try {
			commands.search(ALIAS, "*");
			Assert.fail("Alias was not removed");
		} catch (RedisCommandExecutionException e) {
			Assert.assertTrue(e.getMessage().contains("no such index") || 
					  e.getMessage().contains("Unknown Index name"));
		}
	}

	@Test
	public void testUpdateAlias() {
		testAddAlias();
		String newAlias = "alias456";
		commands.aliasUpdate(newAlias, INDEX);
		Assert.assertTrue(commands.search(newAlias, "*").size() > 0);
	}

}
