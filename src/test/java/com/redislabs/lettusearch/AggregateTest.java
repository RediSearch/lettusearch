package com.redislabs.lettusearch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.AggregateResults;
import com.redislabs.lettusearch.aggregate.Group;
import com.redislabs.lettusearch.aggregate.Sort;
import com.redislabs.lettusearch.aggregate.SortProperty;
import com.redislabs.lettusearch.aggregate.reducer.Avg;

public class AggregateTest extends BaseTest {

	@Test
	public void testAggregateLoad() {
		AggregateResults<String, String> results = connection.sync().aggregate(INDEX, "*",
				AggregateOptions.builder().load(FIELD_NAME).load(FIELD_STYLE).build());
		assertEquals(1, results.getCount());
		assertEquals(beers.size(), results.getResults().size());
		for (int index = 0; index < beers.size(); index++) {
			assertEquals(beers.get(index).get(FIELD_NAME), results.getResults().get(index).get(FIELD_NAME));
			String style = beers.get(index).get(FIELD_STYLE);
			if (style != null) {
				assertEquals(style, results.getResults().get(index).get(FIELD_STYLE));
			}
		}
	}

	@Test
	public void testAggregateGroup() {
		AggregateResults<String, String> results = connection.sync().aggregate(INDEX, "*", AggregateOptions.builder()
				.operation(Group.builder().property(FIELD_STYLE)
						.reduce(Avg.builder().property(FIELD_ABV).as(FIELD_ABV).build()).build())
				.operation(Sort.builder()
						.property(SortProperty.builder().property(FIELD_ABV).order(SortProperty.Order.Desc).build())
						.build())
				.build());
		assertEquals(100, results.getCount());
		assertEquals(100, results.getResults().size());
	}

}
