package com.redislabs.lettusearch;

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

	RedisFuture<AggregateResults<K, V>> aggregate(K index, V query);

	RedisFuture<AggregateResults<K, V>> aggregate(K index, V query, AggregateOptions options);

	RedisFuture<AggregateWithCursorResults<K, V>> aggregate(K index, V query, Cursor cursor);

	RedisFuture<AggregateWithCursorResults<K, V>> aggregate(K index, V query, Cursor cursor, AggregateOptions options);

	RedisFuture<AggregateWithCursorResults<K, V>> cursorRead(K index, long cursor);

	RedisFuture<AggregateWithCursorResults<K, V>> cursorRead(K index, long cursor, long count);

	RedisFuture<String> cursorDelete(K index, long cursor);

}
