package com.redislabs.lettusearch;

import com.redislabs.lettusearch.index.Schema;
import com.redislabs.lettusearch.index.field.TextField;
import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.search.SearchResults;

import static com.redislabs.lettusearch.Beers.*;

public class UsageExample {

    public static void main(String[] args) {
        RediSearchClient client = RediSearchClient.create("redis://localhost"); //<1>
        StatefulRediSearchConnection<String, String> connection = client.connect(); //<2>
        RediSearchCommands<String, String> commands = connection.sync(); //<3>
        commands.create(INDEX, Schema.builder().field(TextField.builder().name(NAME).build()).build()); //<4>
        commands.add(INDEX, Document.builder().id(ID).score(1D).field(NAME, "La Chouffe").build()); //<5>
        SearchResults<String, String> results = commands.search(INDEX, "chou*"); //<6>
        results.forEach(System.out::println);
    }
}
