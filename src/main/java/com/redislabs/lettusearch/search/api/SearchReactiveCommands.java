package com.redislabs.lettusearch.search.api;

import java.util.Map;

import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;

import reactor.core.publisher.Flux;
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

	Mono<String> add(String index, K docId, double score, Map<K, V> fields);

	Mono<String> add(String index, K docId, double score, Map<K, V> fields, V payload);

	Mono<String> add(String index, K docId, double score, Map<K, V> fields, AddOptions options);

	Mono<String> add(String index, K docId, double score, Map<K, V> fields, AddOptions options, V payload);

	Mono<Boolean> del(String index, K docId, boolean deleteDoc);

	Mono<Map<K, V>> get(String index, K docId);

	Flux<Map<K, V>> ftMget(String index, @SuppressWarnings("unchecked") K... docIds);

	Mono<SearchResults<K, V>> search(String index, String query);

	Mono<SearchResults<K, V>> search(String index, String query, SearchOptions options);
}