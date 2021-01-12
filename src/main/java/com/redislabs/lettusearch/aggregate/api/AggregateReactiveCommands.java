package com.redislabs.lettusearch.aggregate.api;

import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.AggregateResults;
import com.redislabs.lettusearch.aggregate.AggregateWithCursorResults;
import com.redislabs.lettusearch.aggregate.Cursor;

import reactor.core.publisher.Mono;

/**
 * Reactive executed commands for RediSearch search index.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 * @since 1.0
 */
public interface AggregateReactiveCommands<K, V> {

	Mono<AggregateResults<K, V>> aggregate(K index, V query);

	Mono<AggregateResults<K, V>> aggregate(K index, V query, AggregateOptions options);

	Mono<AggregateResults<K, V>> aggregate(K index, V query, Object... options);

	Mono<AggregateWithCursorResults<K, V>> aggregate(K index, V query, Cursor cursor);

	Mono<AggregateWithCursorResults<K, V>> aggregate(K index, V query, Cursor cursor, AggregateOptions options);

	Mono<AggregateWithCursorResults<K, V>> aggregate(K index, V query, Cursor cursor, Object... options);

	Mono<AggregateWithCursorResults<K, V>> cursorRead(K index, long cursor);

	Mono<AggregateWithCursorResults<K, V>> cursorRead(K index, long cursor, Long count);

	Mono<String> cursorDelete(K index, long cursor);

}