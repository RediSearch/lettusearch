package com.redislabs.lettusearch;

/**
 * Synchronously executed commands for RediSearch search index.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 * @since 1.0
 */
public interface AggregateCommands<K, V> {

	AggregateResults<K, V> aggregate(K index, V query);

	AggregateResults<K, V> aggregate(K index, V query, AggregateOptions options);

	AggregateWithCursorResults<K, V> aggregate(K index, V query, Cursor cursor);

	AggregateWithCursorResults<K, V> aggregate(K index, V query, Cursor cursor, AggregateOptions options);

	AggregateWithCursorResults<K, V> cursorRead(K index, long cursor);

	AggregateWithCursorResults<K, V> cursorRead(K index, long cursor, long count);

	String cursorDelete(K index, long cursor);

}
