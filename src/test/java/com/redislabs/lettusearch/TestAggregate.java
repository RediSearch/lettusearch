package com.redislabs.lettusearch;

import com.redislabs.lettusearch.aggregate.*;
import com.redislabs.lettusearch.aggregate.reducer.Avg;
import com.redislabs.lettusearch.protocol.CommandKeyword;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static com.redislabs.lettusearch.Beers.*;
import static com.redislabs.lettusearch.protocol.RediSearchCommandArgs.property;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestAggregate extends AbstractBaseTest {

    @Test
    public void testAggregateLoad() {
        AggregateResults<String, String> results = commands.aggregate(INDEX, "*",
                AggregateOptions.builder().load(FIELD_NAME).load(FIELD_STYLE).build());
        Assertions.assertEquals(1, results.getCount());
        assertEquals(beers.size(), results.size());
        for (int index = 0; index < beers.size(); index++) {
            assertEquals(beers.get(index).get(FIELD_NAME).toLowerCase(),
                    results.get(index).get(FIELD_NAME).toLowerCase());
            String style = beers.get(index).get(FIELD_STYLE);
            if (style != null) {
                assertEquals(style.toLowerCase(), results.get(index).get(FIELD_STYLE).toLowerCase());
            }
        }
    }

    @Test
    public void testAggregateRaw() {
        AggregateResults<String, String> results = commands.aggregate(INDEX, "*", CommandKeyword.LOAD, 2,
                property(FIELD_NAME), property(FIELD_STYLE));
        Assertions.assertEquals(1, results.getCount());
        assertEquals(beers.size(), results.size());
        for (int index = 0; index < beers.size(); index++) {
            assertEquals(beers.get(index).get(FIELD_NAME).toLowerCase(),
                    results.get(index).get(FIELD_NAME).toLowerCase());
            String style = beers.get(index).get(FIELD_STYLE);
            if (style != null) {
                assertEquals(style.toLowerCase(), results.get(index).get(FIELD_STYLE).toLowerCase());
            }
        }
    }

    @Test
    public void testAggregateGroup() {
        AggregateResults<String, String> results = commands.aggregate(INDEX, "*", AggregateOptions.builder()
                .operation(Group.builder().property(FIELD_STYLE)
                        .reducer(Avg.builder().property(FIELD_ABV).as(FIELD_ABV).build()).build())
                .operation(Sort.builder().property(SortProperty.builder().property(FIELD_ABV).order(Order.Desc).build())
                        .build())
                .operation(Limit.builder().num(20).offset(0).build()).build());
        assertEquals(100, results.getCount());
        List<Double> abvs = results.stream().map(r -> Double.parseDouble(r.get(FIELD_ABV))).collect(Collectors.toList());
        assertTrue(abvs.get(0) > abvs.get(abvs.size() - 1));
        assertEquals(20, results.size());
    }

    @Test
    public void testAggregateWithCursor() {
        AggregateWithCursorResults<String, String> results = commands.aggregate(INDEX, "*", Cursor.builder().build(),
                AggregateOptions.builder().load(FIELD_ID).load(FIELD_NAME).load(FIELD_ABV).build());
        assertEquals(1, results.getCount());
        assertEquals(1000, results.size());
        assertEquals("harpoon ipa (2010)", results.get(999).get("name").toLowerCase());
        assertEquals("0.086", results.get(9).get("abv"));
        results = commands.cursorRead(INDEX, results.getCursor(), null);
        assertEquals(1000, results.size());
        String deleteStatus = commands.cursorDelete(INDEX, results.getCursor());
        assertEquals("OK", deleteStatus);
    }

    @Test
    public void testAggregateWithCursorRaw() {
        AggregateWithCursorResults<String, String> results = commands.aggregate(INDEX, "*", Cursor.builder().build(),
                CommandKeyword.LOAD, 3, property(FIELD_ID), property(FIELD_NAME), property(FIELD_ABV));
        assertEquals(1, results.getCount());
        assertEquals(1000, results.size());
        assertEquals("harpoon ipa (2010)", results.get(999).get("name").toLowerCase());
        assertEquals("0.086", results.get(9).get("abv"));
        results = commands.cursorRead(INDEX, results.getCursor(), null);
        assertEquals(1000, results.size());
        String deleteStatus = commands.cursorDelete(INDEX, results.getCursor());
        assertEquals("OK", deleteStatus);
    }

}
