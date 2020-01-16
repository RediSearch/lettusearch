package com.redislabs.lettusearch.aggregate.api;

import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.AggregateResults;
import com.redislabs.lettusearch.aggregate.AggregateWithCursorResults;
import com.redislabs.lettusearch.aggregate.CursorOptions;

/**
 * Synchronously executed commands for RediSearch search index.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 * @since 1.0
 */
public interface AggregateCommands<K, V> {

	AggregateResults<K, V> aggregate(String index, String query, AggregateOptions options);

	AggregateWithCursorResults<K, V> aggregate(String index, String query, AggregateOptions options,
			CursorOptions cursorOptions);

	AggregateWithCursorResults<K, V> cursorRead(String index, long cursor);

	AggregateWithCursorResults<K, V> cursorRead(String index, long cursor, long count);

	String cursorDelete(String index, long cursor);

}
