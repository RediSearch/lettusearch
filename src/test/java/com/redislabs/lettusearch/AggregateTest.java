package com.redislabs.lettusearch;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.AggregateResults;
import com.redislabs.lettusearch.aggregate.Group;
import com.redislabs.lettusearch.aggregate.Order;
import com.redislabs.lettusearch.aggregate.Reduce;
import com.redislabs.lettusearch.aggregate.Sort;
import com.redislabs.lettusearch.aggregate.SortProperty;
import com.redislabs.lettusearch.aggregate.reduce.Avg;
import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.Schema;
import com.redislabs.lettusearch.search.Schema.SchemaBuilder;
import com.redislabs.lettusearch.search.field.NumericField;
import com.redislabs.lettusearch.search.field.TextField;

public class AggregateTest {

//	private final static String FIELD_INDEX = "index";
	private final static String FIELD_ABV = "abv";
//	private final static String FIELD_IBU = "ibu";
	private final static String FIELD_ID = "id";
	private final static String FIELD_NAME = "name";
	private final static String FIELD_STYLE = "style";
//	private final static String FIELD_BREWERY_ID = "brewery_id";
	private final static String FIELD_OUNCES = "ounces";

	private final static String INDEX = "beers";

	private RediSearchClient client;
	private StatefulRediSearchConnection<String, String> connection;
	private List<Map<String, String>> beers;

	@Before
	public void setup() throws IOException {
		this.beers = loadBeers("beers.csv");
		client = RediSearchClient.create("redis://localhost");
		connection = client.connect();
		RediSearchCommands<String, String> commands = connection.sync();
		commands.flushall();
		SchemaBuilder schema = Schema.builder();
		schema.field(TextField.builder().name(FIELD_NAME).sortable(true).build());
		schema.field(TextField.builder().name(FIELD_STYLE).sortable(true).build());
		schema.field(NumericField.builder().name(FIELD_ABV).sortable(true).build());
		schema.field(NumericField.builder().name(FIELD_OUNCES).sortable(true).build());
		commands.create(INDEX, schema.build());
		for (Map<String, String> beer : beers) {
			Map<String, String> cleanBeer = new HashMap<>();
			for (Entry<String, String> entry : beer.entrySet()) {
				if (entry.getValue() == null) {
					continue;
				}
				cleanBeer.put(entry.getKey(), entry.getValue());
			}
			commands.add(INDEX, cleanBeer.get(FIELD_ID), 1, cleanBeer, AddOptions.builder().build());
		}
	}

	private List<Map<String, String>> loadBeers(String fileName) throws IOException {
		CsvSchema schema = CsvSchema.builder().setUseHeader(true).setNullValue("").build();
		CsvMapper mapper = new CsvMapper();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
		MappingIterator<Map<String, String>> readValues = mapper.readerFor(Map.class).with(schema)
				.readValues(inputStream);
		return readValues.readAll();
	}

	@After
	public void teardown() {
		if (connection != null) {
			connection.close();
		}
		if (client != null) {
			client.shutdown();
		}
	}

	@Test
	public void testAggregateLoad() {
		AggregateResults<String, String> results = connection.sync().aggregate(INDEX, "*",
				AggregateOptions.builder().load(FIELD_NAME).load(FIELD_STYLE).build());
		assertEquals(1, results.getCount());
		assertEquals(beers.size(), results.getResults().size());
		for (int index = 0; index < beers.size(); index++) {
			assertEquals(beers.get(index).get(FIELD_NAME).toLowerCase(),
					results.getResults().get(index).get(FIELD_NAME));
			String style = beers.get(index).get(FIELD_STYLE);
			if (style != null) {
				assertEquals(style.toLowerCase(), results.getResults().get(index).get(FIELD_STYLE));
			}
		}
	}

	@Test
	public void testAggregateGroup() {
		AggregateResults<String, String> results = connection.sync().aggregate(INDEX, "*", AggregateOptions.builder()
				.operation(Group.builder().property(FIELD_STYLE)
						.reduce(Reduce.builder().function(Avg.builder().property(FIELD_ABV).build()).as(FIELD_ABV)
								.build())
						.build())
				.operation(Sort.builder().property(SortProperty.builder().property(FIELD_ABV).order(Order.Desc).build())
						.build())
				.build());
		assertEquals(100, results.getCount());
		assertEquals(10, results.getResults().size());
	}

}
