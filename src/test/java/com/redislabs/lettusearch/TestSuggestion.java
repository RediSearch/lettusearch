package com.redislabs.lettusearch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.redislabs.lettusearch.suggest.SuggetOptions;
import com.redislabs.lettusearch.suggest.Suggestion;

public class TestSuggestion extends AbstractBaseTest {

	@Test
	public void get() {
		List<Suggestion<String>> results = sync.sugget(SUGINDEX, "Ame",
				SuggetOptions.builder().max(1000L).withScores(true).build());
		assertEquals(8, results.size());
		assertEquals("American Hero", results.get(0).getString());
	}

	@Test
	public void del() {
		Boolean result = sync.sugdel(SUGINDEX, "American Hero");
		assertTrue(result);
		List<Suggestion<String>> results = sync.sugget(SUGINDEX, "Ame", SuggetOptions.builder().max(1000L).build());
		assertEquals(7, results.size());
	}

	@Test
	public void len() {
		long length = sync.suglen(SUGINDEX);
		assertEquals(2245, length);
	}

}
