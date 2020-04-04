package com.redislabs.lettusearch.aggregate.api;

import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.AggregateResults;
import com.redislabs.lettusearch.aggregate.AggregateWithCursorResults;
import com.redislabs.lettusearch.aggregate.Cursor;

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

    RedisFuture<AggregateResults<K, V>> aggregate(K index, V query, AggregateOptions options);

    RedisFuture<AggregateResults<K, V>> aggregate(K index, V query, Object... options);

    RedisFuture<AggregateWithCursorResults<K, V>> aggregate(K index, V query, Cursor cursor,
                                                            AggregateOptions options);

    RedisFuture<AggregateWithCursorResults<K, V>> aggregate(K index, V query, Cursor cursor,
                                                            Object... options);

    RedisFuture<AggregateWithCursorResults<K, V>> cursorRead(K index, long cursor, Long count);

    RedisFuture<String> cursorDelete(K index, long cursor);

}
