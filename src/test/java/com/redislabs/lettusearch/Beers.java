package com.redislabs.lettusearch;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class Beers {

	public final static String FIELD_ABV = "abv";
	public final static String FIELD_ID = "id";
	public final static String FIELD_NAME = "name";
	public final static String FIELD_STYLE = "style";
	public final static String FIELD_OUNCES = "ounces";
	public final static String INDEX = "beers";
	private static final String BEERS_FILENAME = "beers.csv";

	public static List<Map<String, String>> load() throws IOException {
		CsvSchema schema = CsvSchema.builder().setUseHeader(true).setNullValue("").build();
		CsvMapper mapper = new CsvMapper();
		InputStream inputStream = AbstractBaseTest.class.getClassLoader().getResourceAsStream(BEERS_FILENAME);
		MappingIterator<Map<String, String>> iterator = mapper.readerFor(Map.class).with(schema)
				.readValues(inputStream);
		List<Map<String, String>> beers = new ArrayList<>();
		iterator.forEachRemaining(b -> {
			if (b.get(FIELD_ABV) != null) {
				beers.add(b);
			}
		});
		return beers;
	}

}
