package com.redislabs.lettusearch.aggregate.api.sync;

import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.AggregateResults;

import io.lettuce.core.api.sync.RedisCommands;

/**
 * Synchronously executed commands for search.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 */
public interface AggregateCommands<K, V> extends RedisCommands<K, V> {

	AggregateResults<K, V> aggregate(String index, String query, AggregateOptions options);

}
