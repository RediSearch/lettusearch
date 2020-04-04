package com.redislabs.lettusearch.aggregate.api;

import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.AggregateResults;
import com.redislabs.lettusearch.aggregate.AggregateWithCursorResults;
import com.redislabs.lettusearch.aggregate.Cursor;

/**
 * Synchronously executed commands for RediSearch search index.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 * @since 1.0
 */
public interface AggregateCommands<K, V> {

	AggregateResults<K, V> aggregate(K index, V query, AggregateOptions options);

	AggregateResults<K, V> aggregate(K index, V query, Object... options);

	AggregateWithCursorResults<K, V> aggregate(K index, V query, Cursor cursor, AggregateOptions options);

	AggregateWithCursorResults<K, V> aggregate(K index, V query, Cursor cursor, Object... options);

	AggregateWithCursorResults<K, V> cursorRead(K index, long cursor, Long count);

	String cursorDelete(K index, long cursor);

}
