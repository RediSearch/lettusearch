package com.redislabs.lettusearch.search.api.reactive;

import java.util.Map;

import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.DropOptions;
import com.redislabs.lettusearch.search.Schema;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;

import io.lettuce.core.api.reactive.RedisReactiveCommands;
import reactor.core.publisher.Mono;

public interface SearchReactiveCommands<K, V> extends RedisReactiveCommands<K, V> {

	Mono<String> create(String index, Schema schema);

	Mono<String> drop(String index, DropOptions options);

	Mono<String> add(String index, K docId, double score, Map<K, V> fields, AddOptions document);

	Mono<SearchResults<K, V>> search(String index, String query, SearchOptions options);

}