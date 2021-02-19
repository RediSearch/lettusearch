package com.redislabs.lettusearch;

import com.redislabs.lettusearch.suggest.Suggestion;
import com.redislabs.lettusearch.suggest.SuggetOptions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestSuggest extends AbstractBaseTest {

    @Test
    public void testSugget() throws IOException {
        createBeerSuggestions();
        List<Suggestion<String>> results = sync.sugget(SUGINDEX, "Ame", SuggetOptions.builder().max(1000L).build());
        assertEquals(8, results.size());
    }

    @Test
    public void testSuggetWithScores() throws IOException {
        createBeerSuggestions();
        List<Suggestion<String>> results = sync.sugget(SUGINDEX, "Ame", SuggetOptions.builder().max(1000L).withScores(true).build());
        assertEquals(8, results.size());
        assertEquals("American Hero", results.get(0).getString());
    }

    @Test
    public void testSuglen() throws IOException {
        createBeerSuggestions();
        long length = sync.suglen(SUGINDEX);
        assertEquals(2245, length);
    }

    @Test
    public void testSugdel() throws IOException {
        createBeerSuggestions();
        Boolean result = sync.sugdel(SUGINDEX, "American Hero");
        assertTrue(result);
    }

}
