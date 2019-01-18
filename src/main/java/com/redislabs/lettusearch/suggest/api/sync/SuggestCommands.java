package com.redislabs.lettusearch.suggest.api.sync;

import java.util.List;

import com.redislabs.lettusearch.suggest.GetOptions;
import com.redislabs.lettusearch.suggest.SuggestResult;
import com.redislabs.lettusearch.suggest.Suggestion;

import io.lettuce.core.api.sync.RedisCommands;

/**
 * Synchronously executed commands for Suggestions.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 */
public interface SuggestCommands<K, V> extends RedisCommands<K, V> {

	Long add(String key, Suggestion suggestion);

	List<SuggestResult<V>> get(K key, V prefix, GetOptions options);

}
