package com.redislabs.lettusearch.aggregate.api;

import com.redislabs.lettusearch.aggregate.AggregateArgs;
import com.redislabs.lettusearch.aggregate.AggregateResults;
import com.redislabs.lettusearch.aggregate.AggregateWithCursorResults;
import com.redislabs.lettusearch.aggregate.Cursor;
import com.redislabs.lettusearch.aggregate.CursorArgs;

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

	RedisFuture<AggregateResults<K, V>> aggregate(String index, AggregateArgs args);

	RedisFuture<AggregateWithCursorResults<K, V>> aggregate(String index, AggregateArgs args, Cursor cursor);

	RedisFuture<AggregateWithCursorResults<K, V>> cursorRead(String index, CursorArgs args);

	RedisFuture<String> cursorDelete(String index, long cursor);

}
