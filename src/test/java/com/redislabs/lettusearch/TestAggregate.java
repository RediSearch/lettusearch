package com.redislabs.lettusearch;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestAggregate extends AbstractBaseTest {

    private static List<Map<String, String>> beers;

    @BeforeAll
    public static void initializeIndex() throws IOException {
        beers = createBeerIndex();
    }

    @Test
    public void testLoad() {
        AggregateResults<String, String> results = sync.aggregate(INDEX, "*", AggregateOptions.builder().load(ID).load(NAME).load(STYLE).build());
        Assertions.assertEquals(1, results.getCount());
        assertEquals(BEER_COUNT, results.size());
        Map<String, Map<String, String>> beerMap = beers.stream().collect(Collectors.toMap(b -> b.get(ID), b -> b));
        for (Map<String, String> result : results) {
            Map<String,String> beer = beerMap.get(result.get(ID));
            assertEquals(beer.get(NAME).toLowerCase(), result.get(NAME).toLowerCase());
            String style = beer.get(STYLE);
            if (style != null) {
                assertEquals(style.toLowerCase(), result.get(STYLE).toLowerCase());
            }
        }
    }

    @Test
    public void group() {
        AggregateResults<String, String> results = sync.aggregate(INDEX, "*", AggregateOptions.builder().groupBy(Collections.singletonList(STYLE), AggregateOptions.Operation.GroupBy.Reducer.Avg.builder().property(ABV).as(ABV).build()).sortBy(AggregateOptions.Operation.SortBy.Property.builder().property(ABV).order(AggregateOptions.Operation.Order.Desc).build()).limit(0, 20).build());
        assertEquals(100, results.getCount());
        List<Double> abvs = results.stream().map(r -> Double.parseDouble(r.get(ABV))).collect(Collectors.toList());
        assertTrue(abvs.get(0) > abvs.get(abvs.size() - 1));
        assertEquals(20, results.size());
    }

    @Test
    public void cursor() {
        AggregateWithCursorResults<String, String> results = sync.aggregate(INDEX, "*", Cursor.builder().build(), AggregateOptions.builder().load(ID).load(NAME).load(ABV).build());
        assertEquals(1, results.getCount());
        assertEquals(1000, results.size());
        assertEquals("harpoon ipa (2010)", results.get(999).get("name").toLowerCase());
        assertEquals("0.086", results.get(9).get("abv"));
        results = sync.cursorRead(INDEX, results.getCursor());
        assertEquals(1000, results.size());
        String deleteStatus = sync.cursorDelete(INDEX, results.getCursor());
        assertEquals("OK", deleteStatus);
    }

}
