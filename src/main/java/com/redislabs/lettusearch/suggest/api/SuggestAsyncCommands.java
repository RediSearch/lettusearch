package com.redislabs.lettusearch.suggest.api;

import java.util.List;

import com.redislabs.lettusearch.suggest.SugaddArgs;
import com.redislabs.lettusearch.suggest.SuggetArgs;
import com.redislabs.lettusearch.suggest.SuggetOptions;
import com.redislabs.lettusearch.suggest.SuggetResult;

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

	RedisFuture<Long> sugadd(K key, SugaddArgs<V> args);

	RedisFuture<Long> sugadd(K key, V string, double score, boolean increment, V payload);

	RedisFuture<List<SuggetResult<V>>> sugget(K key, SuggetArgs<V> args);

	RedisFuture<List<SuggetResult<V>>> sugget(K key, V prefix, SuggetOptions options);

	RedisFuture<Boolean> sugdel(K key, V string);

	RedisFuture<Long> suglen(K key);

}