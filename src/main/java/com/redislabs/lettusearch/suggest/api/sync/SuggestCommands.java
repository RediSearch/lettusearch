package com.redislabs.lettusearch.suggest.api.sync;

import java.util.List;

import com.redislabs.lettusearch.suggest.SuggestGetOptions;
import com.redislabs.lettusearch.suggest.SuggestResult;

import io.lettuce.core.api.sync.RedisCommands;

/**
 * Synchronously executed commands for Suggestions.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 */
public interface SuggestCommands<K, V> extends RedisCommands<K, V> {

	Long sugadd(K key, V string, double score);

	Long sugadd(K key, V string, double score, V payload);

	Long sugadd(K key, V string, double score, boolean increment);

	Long sugadd(K key, V string, double score, boolean increment, V payload);

	List<SuggestResult<V>> sugget(K key, V prefix, SuggestGetOptions options);

}
