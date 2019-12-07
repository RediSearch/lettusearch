package com.redislabs.lettusearch;

import static com.redislabs.lettusearch.Beers.FIELD_ABV;
import static com.redislabs.lettusearch.Beers.FIELD_ID;
import static com.redislabs.lettusearch.Beers.FIELD_NAME;
import static com.redislabs.lettusearch.Beers.FIELD_STYLE;
import static com.redislabs.lettusearch.Beers.INDEX;
import static com.redislabs.lettusearch.Beers.load;

import com.redislabs.lettusearch.search.Schema;
import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.search.field.Field;
import com.redislabs.lettusearch.search.field.PhoneticMatcher;

public class UsageExample {

	public final static Schema SCHEMA = Schema.builder()
			.field(Field.text(FIELD_NAME).matcher(PhoneticMatcher.English))
			.field(Field.tag(FIELD_STYLE).sortable(true))
			.field(Field.numeric(FIELD_ABV).sortable(true))
			.build();

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
