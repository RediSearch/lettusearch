package com.redislabs.lettusearch.api.reactive;

import com.redislabs.lettusearch.StatefulSearchConnection;
import com.redislabs.lettusearch.api.Document;
import com.redislabs.lettusearch.api.DropOptions;
import com.redislabs.lettusearch.api.Schema;
import com.redislabs.lettusearch.api.Suggestion;

import io.lettuce.core.api.reactive.RedisReactiveCommands;
import reactor.core.publisher.Mono;

public interface SearchReactiveCommands<K, V> extends RedisReactiveCommands<K, V> {

	Mono<String> create(String index, Schema schema);

	Mono<String> drop(String index, DropOptions options);

	Mono<String> add(String index, Document document);

	Mono<Long> add(String key, Suggestion suggestion);

	StatefulSearchConnection<K, V> getStatefulConnection();
}