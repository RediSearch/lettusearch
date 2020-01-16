package com.redislabs.lettusearch.aggregate.api;

import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.AggregateResults;
import com.redislabs.lettusearch.aggregate.AggregateWithCursorResults;
import com.redislabs.lettusearch.aggregate.CursorOptions;

import io.lettuce.core.RedisFuture;

/**
 * Asynchronously executed commands for RediSearch search index.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 * @since 1.0
 */
public interface AggregateAsyncCommands<K, V> {

	RedisFuture<AggregateResults<K, V>> aggregate(String index, String query, AggregateOptions options);

	RedisFuture<AggregateWithCursorResults<K, V>> aggregate(String index, String query, AggregateOptions options,
			CursorOptions cursorOptions);

	RedisFuture<AggregateWithCursorResults<K, V>> cursorRead(String index, long cursor);

	RedisFuture<AggregateWithCursorResults<K, V>> cursorRead(String index, long cursor, long count);

	RedisFuture<String> cursorDelete(String index, long cursor);

}
