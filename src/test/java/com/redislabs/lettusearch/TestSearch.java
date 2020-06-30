package com.redislabs.lettusearch;

import com.redislabs.lettusearch.search.*;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.redislabs.lettusearch.Beers.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestSearch extends AbstractBaseTest {

    @Test
    public void phoneticFields() {
        SearchResults<String, String> results = commands.search(INDEX, "eldur");
        assertEquals(7, results.getCount());
    }

    @Test
    public void get() {
        Map<String, String> map = commands.get(INDEX, "1836");
        assertEquals("Widmer Brothers Hefeweizen", map.get(NAME));
    }

    @Test
    public void mget() {
        List<Map<String, String>> mapList = commands.ftMget(INDEX, "1836", "1837", "292929292");
        assertEquals(3, mapList.size());
        assertEquals("Widmer Brothers Hefeweizen", mapList.get(0).get(NAME));
        assertEquals("Hefe Black", mapList.get(1).get(NAME));
        Flux<Map<String, String>> source = connection.reactive().ftMget(INDEX, "1836", "1837");
        StepVerifier.create(source).expectNextMatches(beer -> beer.get(NAME).equals("Widmer Brothers Hefeweizen")).expectNextMatches(beer -> beer.get(NAME).equals("Hefe Black")).expectComplete().verify();
    }

    @Test
    public void del() {
        boolean deleted = commands.del(INDEX, "1836", true);
        assertTrue(deleted);
        Map<String, String> map = commands.get(INDEX, "1836");
        assertNull(map);
    }

    @Test
    public void noContent() {
        SearchResults<String, String> results = commands.search(INDEX, "Hefeweizen", SearchOptions.builder().withScores(true).noContent(true).limit(Limit.builder().num(100).build()).build());
        assertEquals(22, results.getCount());
        assertEquals(22, results.size());
        assertEquals("1836", results.get(0).getId());
        assertEquals(1.2, results.get(0).getScore(), 0.000001);
    }

    @Test
    public void withPayloads() {
        SearchResults<String, String> results = commands.search(INDEX, "pale", SearchOptions.builder().withPayloads(true).build());
        assertEquals(256, results.getCount());
        Document<String, String> result1 = results.get(0);
        assertNotNull(result1.get(NAME));
        assertNotNull(result1.getPayload());
        assertEquals(result1.get(NAME), result1.getPayload());
    }

    @Test
    public void returnField() {
        SearchResults<String, String> results = commands.search(INDEX, "pale", SearchOptions.builder().returnField(NAME).returnField(STYLE).build());
        assertEquals(256, results.getCount());
        Document<String, String> result1 = results.get(0);
        assertNotNull(result1.get(NAME));
        assertNotNull(result1.get(STYLE));
        assertNull(result1.get(ABV));
    }


    @Test
    public void invalidReturnField() {
        SearchResults<String, String> results = commands.search(INDEX, "pale", SearchOptions.builder().returnField(NAME).returnField(STYLE).returnField("").build());
        assertEquals(256, results.getCount());
        Document<String, String> result1 = results.get(0);
        assertNotNull(result1.get(NAME));
        assertNotNull(result1.get(STYLE));
        assertNull(result1.get(ABV));
    }

    @Test
    public void inKeys() {
        SearchResults<String, String> results = commands.search(INDEX, "*", SearchOptions.builder().inKeys(Collections.singletonList("1018")).inKey("2593").build());
        assertEquals(2, results.getCount());
        Document<String, String> result1 = results.get(0);
        assertNotNull(result1.get(NAME));
        assertNotNull(result1.get(STYLE));
        assertEquals("0.07", result1.get(ABV));
    }

    @Test
    public void inFields() {
        SearchResults<String, String> results = commands.search(INDEX, "sculpin", SearchOptions.builder().inField(NAME).build());
        assertEquals(2, results.getCount());
        Document<String, String> result1 = results.get(0);
        assertNotNull(result1.get(NAME));
        assertNotNull(result1.get(STYLE));
        assertEquals("0.07", result1.get(ABV));
    }

    @Test
    public void highlight() {
        String term = "pale";
        String query = "@style:" + term;
        TagOptions tagOptions = TagOptions.builder().open("<b>").close("</b>").build();
        SearchResults<String, String> results = commands.search(INDEX, query, SearchOptions.builder().highlight(HighlightOptions.builder().build()).build());
        for (Document<String, String> result : results) {
            assertTrue(highlighted(result, STYLE, tagOptions, term));
        }
        results = commands.search(INDEX, query, SearchOptions.builder().highlight(HighlightOptions.builder().field(NAME).build()).build());
        for (Document<String, String> result : results) {
            assertFalse(highlighted(result, STYLE, tagOptions, term));
        }
        tagOptions = TagOptions.builder().open("[start]").close("[end]").build();
        results = commands.search(INDEX, query, SearchOptions.builder().highlight(HighlightOptions.builder().field(STYLE).tags(tagOptions).build()).build());
        for (Document<String, String> result : results) {
            assertTrue(highlighted(result, STYLE, tagOptions, term));
        }
    }

    private boolean highlighted(Document<String, String> result, String fieldName, TagOptions tags, String string) {
        String fieldValue = result.get(fieldName).toLowerCase();
        return fieldValue.contains(tags.getOpen() + string + tags.getClose());
    }

    @Test
    public void reactive() {
        SearchResults<String, String> results = connection.reactive().search(INDEX, "pale", SearchOptions.builder().limit(Limit.builder().num(100).offset(200).build()).build()).block();
        assertEquals(256, results.getCount());
        Document<String, String> result1 = results.get(0);
        assertNotNull(result1.get(NAME));
        assertNotNull(result1.get(STYLE));
        assertNotNull(result1.get(ABV));
    }

    @Test
    public void phonetic() {
        SearchResults<String, String> results = commands.search(Beers.INDEX, "pail");
        assertEquals(256, results.getCount());
    }

}
