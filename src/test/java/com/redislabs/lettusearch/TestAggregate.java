package com.redislabs.lettusearch;

import static com.redislabs.lettusearch.Beers.FIELD_ABV;
import static com.redislabs.lettusearch.Beers.FIELD_ID;
import static com.redislabs.lettusearch.Beers.FIELD_NAME;
import static com.redislabs.lettusearch.Beers.FIELD_STYLE;
import static com.redislabs.lettusearch.Beers.INDEX;
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
		AggregateResults<String, String> results = commands.aggregate(INDEX, "*",
				new AggregateOptions().load(FIELD_NAME).load(FIELD_STYLE));
		assertEquals(1, results.count());
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
		AggregateResults<String, String> results = commands.aggregate(INDEX, "*", new AggregateOptions()
				.operation(new Group().property(FIELD_STYLE).reducer(new Avg().property(FIELD_ABV).as(FIELD_ABV)))
				.operation(new Sort().property(new SortProperty().property(FIELD_ABV).order(Order.Desc)))
				.operation(new Limit().num(20).offset(0)));
		assertEquals(100, results.count());
		assertEquals(20, results.size());
	}

	@Test
	public void testAggregateWithCursor() {
		AggregateWithCursorResults<String, String> results = commands.aggregate(INDEX, "*",
				new AggregateOptions().load(FIELD_ID).load(FIELD_NAME).load(FIELD_ABV), new CursorOptions());
		assertEquals(1, results.count());
		assertEquals(1000, results.size());
		assertEquals("harpoon ipa (2010)", results.get(999).get("name").toLowerCase());
		assertEquals("0.086", results.get(9).get("abv"));
		results = commands.cursorRead(INDEX, results.cursor());
		assertEquals(1000, results.size());
		String deleteStatus = commands.cursorDelete(INDEX, results.cursor());
		assertEquals("OK", deleteStatus);
	}

}
