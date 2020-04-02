package com.redislabs.lettusearch;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.redislabs.lettusearch.search.Document;

public class Beers {

	public final static String FIELD_ABV = "abv";
	public final static String FIELD_ID = "id";
	public final static String FIELD_NAME = "name";
	public final static String FIELD_STYLE = "style";
	public final static String FIELD_OUNCES = "ounces";
	public final static String INDEX = "beers";
	private static final String BEERS_FILENAME = "beers.csv";

	public static List<Document<String, String>> load() throws IOException {
		CsvSchema schema = CsvSchema.builder().setUseHeader(true).setNullValue("").build();
		CsvMapper mapper = new CsvMapper();
		InputStream inputStream = AbstractBaseTest.class.getClassLoader().getResourceAsStream(BEERS_FILENAME);
		MappingIterator<Document<String, String>> iterator = mapper.readerFor(Document.class).with(schema)
				.readValues(inputStream);
		List<Document<String, String>> beers = new ArrayList<>();
		iterator.forEachRemaining(b -> {
			if (b.get(FIELD_ABV) != null) {
				b.setId(b.get(FIELD_ID));
				b.setScore(1d);
				b.setPayload(b.get(FIELD_NAME));
				beers.add(b);
			}
		});
		return beers;
	}

}
