package com.redislabs.lettusearch.aggregate.api;

import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.AggregateResults;
import com.redislabs.lettusearch.aggregate.AggregateWithCursorResults;
import com.redislabs.lettusearch.aggregate.CursorOptions;

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

	Mono<AggregateResults<K, V>> aggregate(String index, String query, AggregateOptions options);

	Mono<AggregateWithCursorResults<K, V>> aggregate(String index, String query, AggregateOptions options,
			CursorOptions cursorOptions);

	Mono<AggregateWithCursorResults<K, V>> cursorRead(String index, long cursor);

	Mono<AggregateWithCursorResults<K, V>> cursorRead(String index, long cursor, long count);

	Mono<String> cursorDelete(String index, long cursor);

}