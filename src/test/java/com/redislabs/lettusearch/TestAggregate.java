package com.redislabs.lettusearch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.AggregateResults;
import com.redislabs.lettusearch.aggregate.AggregateWithCursorResults;
import com.redislabs.lettusearch.aggregate.CursorOptions;
import com.redislabs.lettusearch.aggregate.Group;
import com.redislabs.lettusearch.aggregate.Limit;
import com.redislabs.lettusearch.aggregate.Order;
import com.redislabs.lettusearch.aggregate.Sort;
import com.redislabs.lettusearch.aggregate.SortProperty;
import com.redislabs.lettusearch.aggregate.reducer.Avg;

public class TestAggregate extends AbstractBaseTest {

	@Test
	public void testAggregateLoad() {
		AggregateResults<String, String> results = connection.sync().aggregate(INDEX, "*",
				AggregateOptions.builder().load(FIELD_NAME).load(FIELD_STYLE).build());
		assertEquals(1, results.getCount());
		assertEquals(beers.size(), results.getResults().size());
		for (int index = 0; index < beers.size(); index++) {
			assertEquals(beers.get(index).get(FIELD_NAME).toLowerCase(),
					results.getResults().get(index).get(FIELD_NAME).toLowerCase());
			String style = beers.get(index).get(FIELD_STYLE);
			if (style != null) {
				assertEquals(style.toLowerCase(), results.getResults().get(index).get(FIELD_STYLE).toLowerCase());
			}
		}
	}

	@Test
	public void testAggregateGroup() {
		AggregateResults<String, String> results = connection.sync().aggregate(INDEX, "*", AggregateOptions.builder()
				.operation(Group.builder().property(FIELD_STYLE)
						.reduce(Avg.builder().property(FIELD_ABV).as(FIELD_ABV).build()).build())
				.operation(Sort.builder().property(SortProperty.builder().property(FIELD_ABV).order(Order.Desc).build())
						.build())
				.operation(Limit.builder().num(20).offset(0).build()).build());
		assertEquals(100, results.getCount());
		assertEquals(20, results.getResults().size());
	}

	@Test
	public void testAggregateWithCursor() {
		AggregateWithCursorResults<String, String> results = connection.sync().aggregate(INDEX, "*",
				AggregateOptions.builder().load(FIELD_ID).load(FIELD_NAME).load(FIELD_ABV).build(),
				CursorOptions.builder().build());
		assertEquals(1, results.getResults().getCount());
		assertEquals(1000, results.getResults().getResults().size());
		assertEquals("lemon shandy tripel", results.getResults().getResults().get(999).get("name"));
		assertEquals("0.086", results.getResults().getResults().get(9).get("abv"));
		results = connection.sync().cursorRead(INDEX, results.getCursor());
		assertEquals(1000, results.getResults().getResults().size());
		String deleteStatus = connection.sync().cursorDelete(INDEX, results.getCursor());
		assertEquals("OK", deleteStatus);
	}

}
