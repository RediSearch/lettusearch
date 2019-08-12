package com.redislabs.lettusearch.suggest.api.async;

import java.util.List;

import com.redislabs.lettusearch.suggest.SuggestGetOptions;
import com.redislabs.lettusearch.suggest.SuggestResult;

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

	RedisFuture<Long> sugadd(K key, V string, double score);

	RedisFuture<Long> sugadd(K key, V string, double score, boolean increment);

	RedisFuture<Long> sugadd(K key, V string, double score, V payload);

	RedisFuture<Long> sugadd(K key, V string, double score, boolean increment, V payload);

	RedisFuture<List<SuggestResult<V>>> sugget(K key, V prefix, SuggestGetOptions options);

}