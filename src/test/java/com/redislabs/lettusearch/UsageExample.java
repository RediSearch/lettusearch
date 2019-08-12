package com.redislabs.lettusearch;

import static com.redislabs.lettusearch.Beers.FIELD_ABV;
import static com.redislabs.lettusearch.Beers.FIELD_ID;
import static com.redislabs.lettusearch.Beers.FIELD_NAME;
import static com.redislabs.lettusearch.Beers.FIELD_STYLE;
import static com.redislabs.lettusearch.Beers.INDEX;
import static com.redislabs.lettusearch.Beers.load;

import com.redislabs.lettusearch.search.Schema;
import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.search.field.NumericField;
import com.redislabs.lettusearch.search.field.PhoneticMatcher;
import com.redislabs.lettusearch.search.field.TagField;
import com.redislabs.lettusearch.search.field.TextField;

public class UsageExample {

	public final static Schema SCHEMA = Schema.builder()
			.field(TextField.builder().name(FIELD_NAME).matcher(PhoneticMatcher.English).build())
			.field(TagField.builder().name(FIELD_STYLE).sortable(true).build())
			.field(NumericField.builder().name(FIELD_ABV).sortable(true).build()).build();

	public static void main(String[] args) throws Exception {
		RediSearchClient client = RediSearchClient.create("redis://localhost");
		StatefulRediSearchConnection<String, String> conn = client.connect();
		RediSearchCommands<String, String> commands = conn.sync();
		commands.create(Beers.INDEX, SCHEMA);
		load().forEach(d -> commands.add(Beers.INDEX, d.get(FIELD_ID), 1, d));
		SearchResults<String, String> results = commands.search(INDEX, "sculpin");
		results.forEach(r -> System.out.println(r));
	}
}
