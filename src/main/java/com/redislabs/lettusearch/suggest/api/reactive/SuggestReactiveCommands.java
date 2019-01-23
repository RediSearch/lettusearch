package com.redislabs.lettusearch.suggest.api.reactive;

import com.redislabs.lettusearch.suggest.SuggestGetOptions;
import com.redislabs.lettusearch.suggest.SuggestResult;
import com.redislabs.lettusearch.suggest.SuggestAddOptions;

import io.lettuce.core.api.reactive.RedisReactiveCommands;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SuggestReactiveCommands<K, V> extends RedisReactiveCommands<K, V> {

	Mono<Long> sugadd(K key, V string, double score, SuggestAddOptions options);

	Flux<SuggestResult<V>> sugget(K key, V prefix, SuggestGetOptions options);

}