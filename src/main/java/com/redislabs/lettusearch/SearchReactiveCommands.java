package com.redislabs.lettusearch;

import reactor.core.publisher.Mono;

/**
 * Reactive executed commands for RediSearch search index.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 * @since 1.0
 */
public interface SearchReactiveCommands<K, V> {

	Mono<SearchResults<K, V>> search(K index, V query);

	Mono<SearchResults<K, V>> search(K index, V query, SearchOptions<K> options);

}