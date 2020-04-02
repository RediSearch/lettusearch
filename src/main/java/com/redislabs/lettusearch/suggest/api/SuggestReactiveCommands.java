package com.redislabs.lettusearch.suggest.api;

import com.redislabs.lettusearch.suggest.Suggestion;
import com.redislabs.lettusearch.suggest.SuggetOptions;

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

	Mono<Long> sugadd(K key, Suggestion<V> suggestion, boolean increment);

	Flux<Suggestion<V>> sugget(K key, V prefix, SuggetOptions options);

	Mono<Boolean> sugdel(K key, V string);

	Mono<Long> suglen(K key);

}