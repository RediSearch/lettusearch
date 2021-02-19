package com.redislabs.lettusearch;

import java.util.List;

import io.lettuce.core.RedisFuture;

/**
 * Asynchronously executed commands for RediSearch suggestion index.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 * @since 1.0
 */
public interface SuggestAsyncCommands<K, V> {

	RedisFuture<Long> sugadd(K key, Suggestion<V> suggestion);

	RedisFuture<Long> sugadd(K key, Suggestion<V> suggestion, boolean increment);

	RedisFuture<List<Suggestion<V>>> sugget(K key, V prefix);

	RedisFuture<List<Suggestion<V>>> sugget(K key, V prefix, SuggetOptions options);

	RedisFuture<Boolean> sugdel(K key, V string);

	RedisFuture<Long> suglen(K key);

}