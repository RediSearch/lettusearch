package com.redislabs.lettusearch.suggest.api.sync;

import java.util.List;

import com.redislabs.lettusearch.suggest.SuggestGetOptions;
import com.redislabs.lettusearch.suggest.SuggestResult;
import com.redislabs.lettusearch.suggest.SuggestAddOptions;

import io.lettuce.core.api.sync.RedisCommands;

/**
 * Synchronously executed commands for Suggestions.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 */
public interface SuggestCommands<K, V> extends RedisCommands<K, V> {

	Long sugadd(K key, V string, double score, SuggestAddOptions options);

	List<SuggestResult<V>> sugget(K key, V prefix, SuggestGetOptions options);

}
