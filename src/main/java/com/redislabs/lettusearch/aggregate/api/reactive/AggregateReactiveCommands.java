package com.redislabs.lettusearch.aggregate.api.reactive;

import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.AggregateResults;

import io.lettuce.core.api.reactive.RedisReactiveCommands;
import reactor.core.publisher.Mono;

public interface AggregateReactiveCommands<K, V> extends RedisReactiveCommands<K, V> {

	Mono<AggregateResults<K, V>> aggregate(String index, String query, AggregateOptions options);

}