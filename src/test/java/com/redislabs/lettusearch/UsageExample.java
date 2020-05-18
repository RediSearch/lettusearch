package com.redislabs.lettusearch;

import com.redislabs.lettusearch.index.CreateOptions;
import com.redislabs.lettusearch.index.Schema;
import com.redislabs.lettusearch.index.field.NumericField;
import com.redislabs.lettusearch.index.field.PhoneticMatcher;
import com.redislabs.lettusearch.index.field.TagField;
import com.redislabs.lettusearch.index.field.TextField;
import com.redislabs.lettusearch.search.SearchResults;

import static com.redislabs.lettusearch.Beers.*;

public class UsageExample {

    public final static Schema SCHEMA = Schema.builder().field(TextField.builder().name(FIELD_NAME).matcher(PhoneticMatcher.English).build()).field(TagField.builder().name(FIELD_STYLE).sortable(true).build()).field(NumericField.builder().name(FIELD_ABV).sortable(true).build()).build();

    public static void main(String[] args) throws Exception {
        StatefulRediSearchConnection<String, String> conn = RediSearchClient.create("redis://localhost").connect();
        RediSearchCommands<String, String> commands = conn.sync();
        commands.create(Beers.INDEX, SCHEMA, CreateOptions.builder().build());
        load().forEach(d -> commands.add(Beers.INDEX, d, null));
        SearchResults<String, String> results = commands.search(INDEX, "chouf*");
        results.forEach(System.out::println);
    }
}
