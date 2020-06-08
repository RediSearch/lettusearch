package com.redislabs.lettusearch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.suggest.SuggetOptions;
import com.redislabs.lettusearch.suggest.Suggestion;

public class TestSuggestion extends AbstractBaseTest {

    @Test
    public void phoneticFields() {
        SearchResults<String, String> results = commands.search(Beers.INDEX, "pail");
        assertEquals(256, results.getCount());
    }

    @Test
    public void sugget() {
        List<Suggestion<String>> results = commands.sugget(SUGINDEX, "Ame", SuggetOptions.builder().max(1000L).withScores(true).build());
        assertEquals(8, results.size());
        assertEquals("American Hero", results.get(0).getString());
    }

    @Test
    public void sugdel() {
        Boolean result = commands.sugdel(SUGINDEX, "American Hero");
        assertTrue(result);
        List<Suggestion<String>> results = commands.sugget(SUGINDEX, "Ame", SuggetOptions.builder().max(1000L).build());
        assertEquals(7, results.size());
    }

    @Test
    public void suglen() {
        long length = commands.suglen(SUGINDEX);
        assertEquals(2245, length);
    }

}
