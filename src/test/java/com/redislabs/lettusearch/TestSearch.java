package com.redislabs.lettusearch;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class TestSearch extends AbstractBaseTest {

    @BeforeAll
    public static void initializeIndex() throws IOException {
        createBeerIndex();
    }

    @Test
    public void phoneticFields() {
        SearchResults<String, String> results = sync.search(INDEX, "eldur");
        assertEquals(7, results.getCount());
    }

    @Test
    public void noContent() {
        SearchResults<String, String> results = sync.search(INDEX, "Hefeweizen", SearchOptions.<String>builder().withScores(true).noContent(true).limit(SearchOptions.Limit.builder().num(100).build()).build());
        assertEquals(22, results.getCount());
        assertEquals(22, results.size());
        assertEquals("beer:1836", results.get(0).getId());
        assertEquals(12, results.get(0).getScore(), 0.000001);
    }

    @Test
    public void withPayloads() {
        SearchResults<String, String> results = sync.search(INDEX, "pale", SearchOptions.<String>builder().withPayloads(true).build());
        assertEquals(256, results.getCount());
        Document<String, String> result1 = results.get(0);
        assertNotNull(result1.get(NAME));
        assertNotNull(result1.getPayload());
        assertEquals(result1.get(NAME), result1.getPayload());
    }

    @Test
    public void returnField() {
        SearchResults<String, String> results = sync.search(INDEX, "pale", SearchOptions.<String>builder().returnField(NAME).returnField(STYLE).build());
        assertEquals(256, results.getCount());
        Document<String, String> result1 = results.get(0);
        assertNotNull(result1.get(NAME));
        assertNotNull(result1.get(STYLE));
        assertNull(result1.get(ABV));
    }

    @Test
    public void allOptions() {
        SearchOptions<String> options = SearchOptions.<String>builder().withPayloads(true).noStopWords(true).limit(SearchOptions.Limit.builder().num(100).offset(10).build()).withScores(true).highlight(SearchOptions.Highlight.<String>builder().field(NAME).tag(SearchOptions.Highlight.Tag.<String>builder().open("<TAG>").close("</TAG>").build()).build()).language(SearchOptions.Language.English).noContent(false).sortBy(SearchOptions.SortBy.<String>builder().direction(SearchOptions.SortBy.Direction.Ascending).field(NAME).build()).verbatim(false).withSortKeys(true).returnField(NAME).returnField(STYLE).build();
        SearchResults<String, String> results = sync.search(INDEX, "pale", options);
        assertEquals(256, results.getCount());
        Document<String, String> result1 = results.get(0);
        assertNotNull(result1.get(NAME));
        assertNotNull(result1.get(STYLE));
        assertNull(result1.get(ABV));
    }

    @Test
    public void invalidReturnField() {
        SearchResults<String, String> results = sync.search(INDEX, "pale", SearchOptions.<String>builder().returnField(NAME).returnField(STYLE).returnField("").build());
        assertEquals(256, results.getCount());
        Document<String, String> result1 = results.get(0);
        assertNotNull(result1.get(NAME));
        assertNotNull(result1.get(STYLE));
        assertNull(result1.get(ABV));
    }

    @Test
    public void inKeys() {
        SearchResults<String, String> results = sync.search(INDEX, "*", SearchOptions.<String>builder().inKeys(Collections.singletonList("beer:1018")).inKey("beer:2593").build());
        assertEquals(2, results.getCount());
        Document<String, String> result1 = results.get(0);
        assertNotNull(result1.get(NAME));
        assertNotNull(result1.get(STYLE));
        assertEquals("0.07", result1.get(ABV));
    }

    @Test
    public void inFields() {
        SearchResults<String, String> results = sync.search(INDEX, "sculpin", SearchOptions.<String>builder().inField(NAME).build());
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
        SearchOptions.Highlight.Tag<String> tag = SearchOptions.Highlight.Tag.<String>builder().open("<b>").close("</b>").build();
        SearchResults<String, String> results = sync.search(INDEX, query, SearchOptions.<String>builder().highlight(SearchOptions.Highlight.<String>builder().build()).build());
        for (Document<String, String> result : results) {
            assertTrue(highlighted(result, STYLE, tag, term));
        }
        results = sync.search(INDEX, query, SearchOptions.<String>builder().highlight(SearchOptions.Highlight.<String>builder().field(NAME).build()).build());
        for (Document<String, String> result : results) {
            assertFalse(highlighted(result, STYLE, tag, term));
        }
        tag = SearchOptions.Highlight.Tag.<String>builder().open("[start]").close("[end]").build();
        results = sync.search(INDEX, query, SearchOptions.<String>builder().highlight(SearchOptions.Highlight.<String>builder().field(STYLE).tag(tag).build()).build());
        for (Document<String, String> result : results) {
            assertTrue(highlighted(result, STYLE, tag, term));
        }
    }

    private boolean highlighted(Document<String, String> result, String fieldName, SearchOptions.Highlight.Tag<String> tag, String string) {
        String fieldValue = result.get(fieldName).toLowerCase();
        return fieldValue.contains(tag.getOpen() + string + tag.getClose());
    }

    @Test
    public void reactive() {
        SearchResults<String, String> results = connection.reactive().search(INDEX, "pale", SearchOptions.<String>builder().limit(SearchOptions.Limit.builder().num(100).offset(200).build()).build()).block();
        assertEquals(256, results.getCount());
        Document<String, String> result1 = results.get(0);
        assertNotNull(result1.get(NAME));
        assertNotNull(result1.get(STYLE));
        assertNotNull(result1.get(ABV));
    }

    @Test
    public void phonetic() {
        SearchResults<String, String> results = sync.search(INDEX, "pail");
        assertEquals(256, results.getCount());
    }

    @Test
    public void limit00() {
        SearchResults<String, String> results = sync.search(INDEX, "*", SearchOptions.<String>builder().limit(SearchOptions.Limit.builder().num(0).offset(0).build()).build());
        assertEquals(2348, results.getCount());
    }

}
