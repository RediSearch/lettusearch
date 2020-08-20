package com.redislabs.lettusearch;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.redislabs.lettusearch.index.Schema;
import com.redislabs.lettusearch.index.field.NumericField;
import com.redislabs.lettusearch.index.field.PhoneticMatcher;
import com.redislabs.lettusearch.index.field.TagField;
import com.redislabs.lettusearch.index.field.TextField;
import com.redislabs.lettusearch.search.Document;

public class Beers {

	public final static String ABV = "abv";
	public final static String ID = "id";
	public final static String NAME = "name";
	public final static String STYLE = "style";
	public final static String OUNCES = "ounces";
	public final static Schema<String> SCHEMA = Schema.<String>builder()
			.field(TextField.<String>builder().name(NAME).matcher(PhoneticMatcher.English).build())
			.field(TagField.<String>builder().name(STYLE).sortable(true).build())
			.field(NumericField.<String>builder().name(ABV).sortable(true).build()).build();
	public final static String INDEX = "beers";

	public static List<Document<String, String>> load() throws IOException {
		CsvSchema schema = CsvSchema.builder().setUseHeader(true).setNullValue("").build();
		CsvMapper mapper = new CsvMapper();
		InputStream inputStream = AbstractBaseTest.class.getClassLoader().getResourceAsStream("beers.csv");
		MappingIterator<Document<String, String>> iterator = mapper.readerFor(Document.class).with(schema)
				.readValues(inputStream);
		List<Document<String, String>> beers = new ArrayList<>();
		iterator.forEachRemaining(b -> {
			if (b.get(ABV) != null) {
				b.setId(b.get(ID));
				b.setScore(1d);
				b.setPayload(b.get(NAME));
				beers.add(b);
			}
		});
		return beers;
	}

}
