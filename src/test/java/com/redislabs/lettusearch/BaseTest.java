package com.redislabs.lettusearch;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.Schema;
import com.redislabs.lettusearch.search.Schema.SchemaBuilder;
import com.redislabs.lettusearch.search.field.NumericField;
import com.redislabs.lettusearch.search.field.PhoneticMatcher;
import com.redislabs.lettusearch.search.field.TextField;

public class BaseTest {

//	private final static String FIELD_INDEX = "index";
	protected final static String FIELD_ABV = "abv";
//	private final static String FIELD_IBU = "ibu";
	protected final static String FIELD_ID = "id";
	protected final static String FIELD_NAME = "name";
	protected final static String FIELD_STYLE = "style";
//	private final static String FIELD_BREWERY_ID = "brewery_id";
	protected final static String FIELD_OUNCES = "ounces";

	protected final static String INDEX = "beers";

	private RediSearchClient client;
	protected StatefulRediSearchConnection<String, String> connection;
	protected List<Map<String, String>> beers;

	@Before
	public void setup() throws IOException {
		this.beers = loadBeers("beers.csv");
		client = RediSearchClient.create("redis://localhost");
		connection = client.connect();
		RediSearchCommands<String, String> commands = connection.sync();
		commands.flushall();
		SchemaBuilder schema = Schema.builder();
		schema.field(TextField.builder().name(FIELD_NAME).sortable(true).build());
		schema.field(TextField.builder().name(FIELD_STYLE).matcher(PhoneticMatcher.English).sortable(true).build());
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

}
