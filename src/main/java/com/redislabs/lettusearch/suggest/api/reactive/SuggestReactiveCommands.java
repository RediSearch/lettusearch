package com.redislabs.lettusearch.suggest.api.reactive;

import com.redislabs.lettusearch.suggest.SuggestGetOptions;
import com.redislabs.lettusearch.suggest.SuggestResult;

import io.lettuce.core.api.reactive.RedisReactiveCommands;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SuggestReactiveCommands<K, V> extends RedisReactiveCommands<K, V> {

	Mono<Long> sugadd(K key, V string, double score);

	Mono<Long> sugadd(K key, V string, double score, V payload);

	Mono<Long> sugadd(K key, V string, double score, boolean increment);

	Mono<Long> sugadd(K key, V string, double score, boolean increment, V payload);

	Flux<SuggestResult<V>> sugget(K key, V prefix, SuggestGetOptions options);

}