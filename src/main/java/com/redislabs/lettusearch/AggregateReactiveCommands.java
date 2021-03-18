package com.redislabs.lettusearch;

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

	Mono<AggregateResults<K>> aggregate(K index, V query);

	Mono<AggregateResults<K>> aggregate(K index, V query, AggregateOptions options);

	Mono<AggregateWithCursorResults<K>> aggregate(K index, V query, Cursor cursor);

	Mono<AggregateWithCursorResults<K>> aggregate(K index, V query, Cursor cursor, AggregateOptions options);

	Mono<AggregateWithCursorResults<K>> cursorRead(K index, long cursor);

	Mono<AggregateWithCursorResults<K>> cursorRead(K index, long cursor, long count);

	Mono<String> cursorDelete(K index, long cursor);

}