package com.redislabs.lettusearch.suggest.api;

import java.util.List;

import com.redislabs.lettusearch.suggest.Suggestion;
import com.redislabs.lettusearch.suggest.SuggetOptions;

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

	RedisFuture<Long> sugadd(K key, Suggestion<V> suggestion, boolean increment);

	RedisFuture<List<Suggestion<V>>> sugget(K key, V prefix, SuggetOptions options);

	RedisFuture<Boolean> sugdel(K key, V string);

	RedisFuture<Long> suglen(K key);

}