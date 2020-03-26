package com.redislabs.lettusearch;

import static com.redislabs.lettusearch.Beers.FIELD_ABV;
import static com.redislabs.lettusearch.Beers.FIELD_ID;
import static com.redislabs.lettusearch.Beers.FIELD_NAME;
import static com.redislabs.lettusearch.Beers.FIELD_STYLE;
import static com.redislabs.lettusearch.Beers.INDEX;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.redislabs.lettusearch.aggregate.AggregateArgs;
import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.AggregateResults;
import com.redislabs.lettusearch.aggregate.AggregateWithCursorResults;
import com.redislabs.lettusearch.aggregate.Cursor;
import com.redislabs.lettusearch.aggregate.CursorArgs;
import com.redislabs.lettusearch.aggregate.Group;
import com.redislabs.lettusearch.aggregate.Limit;
import com.redislabs.lettusearch.aggregate.Order;
import com.redislabs.lettusearch.aggregate.Sort;
import com.redislabs.lettusearch.aggregate.SortProperty;
import com.redislabs.lettusearch.aggregate.reducer.Avg;
import static org.junit.jupiter.api.Assertions.*;

public class TestAggregate extends AbstractBaseTest {

	@Test
	public void testAggregateLoad() {
		AggregateResults<String, String> results = commands.aggregate(INDEX, AggregateArgs.builder().query("*")
				.options(AggregateOptions.builder().load(FIELD_NAME).load(FIELD_STYLE).build()).build());
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
		AggregateResults<String, String> results = commands.aggregate(INDEX,
				AggregateArgs.builder().query("*").options(AggregateOptions.builder()
						.operation(Group.builder().property(FIELD_STYLE)
								.reducer(Avg.builder().property(FIELD_ABV).as(FIELD_ABV).build()).build())
						.operation(Sort.builder()
								.property(SortProperty.builder().property(FIELD_ABV).order(Order.Desc).build()).build())
						.operation(Limit.builder().num(20).offset(0).build()).build()).build());
		assertEquals(100, results.getCount());
		assertEquals(20, results.size());
	}

	@Test
	public void testAggregateWithCursor() {
		AggregateWithCursorResults<String, String> results = commands.aggregate(INDEX,
				AggregateArgs.builder().query("*")
						.options(AggregateOptions.builder().load(FIELD_ID).load(FIELD_NAME).load(FIELD_ABV).build())
						.build(),
				Cursor.builder().build());
		assertEquals(1, results.getCount());
		assertEquals(1000, results.size());
		assertEquals("harpoon ipa (2010)", results.get(999).get("name").toLowerCase());
		assertEquals("0.086", results.get(9).get("abv"));
		results = commands.cursorRead(INDEX, CursorArgs.builder().cursor(results.getCursor()).build());
		assertEquals(1000, results.size());
		String deleteStatus = commands.cursorDelete(INDEX, results.getCursor());
		assertEquals("OK", deleteStatus);
	}

}
