package com.redislabs.lettusearch.suggest.api;

import com.redislabs.lettusearch.suggest.SugaddArgs;
import com.redislabs.lettusearch.suggest.SuggetArgs;
import com.redislabs.lettusearch.suggest.SuggetOptions;
import com.redislabs.lettusearch.suggest.SuggetResult;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Reactive executed commands for RediSearch suggestion index.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 * @since 1.0
 */
public interface SuggestReactiveCommands<K, V> {

	Mono<Long> sugadd(K key, SugaddArgs<V> args);

	Mono<Long> sugadd(K key, V string, double score, boolean increment, V payload);

	Flux<SuggetResult<V>> sugget(K key, SuggetArgs<V> args);

	Flux<SuggetResult<V>> sugget(K key, V prefix, SuggetOptions options);

	Mono<Boolean> sugdel(K key, V string);

	Mono<Long> suglen(K key);

}