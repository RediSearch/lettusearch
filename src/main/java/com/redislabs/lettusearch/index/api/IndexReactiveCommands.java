package com.redislabs.lettusearch.index.api;

import com.redislabs.lettusearch.index.CreateOptions;
import com.redislabs.lettusearch.index.DropOptions;
import com.redislabs.lettusearch.index.Schema;
import com.redislabs.lettusearch.index.field.FieldOptions;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Reactive-executed index admin commands for RediSearch.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 * @since 1.0
 */
public interface IndexReactiveCommands<K, V> {

	Mono<String> create(K index, Schema schema);

	Mono<String> create(K index, Schema schema, CreateOptions options);

	Mono<String> drop(K index);

	Mono<String> drop(K index, DropOptions options);

	Mono<String> alter(K index, K field, FieldOptions options);

	Flux<Object> ftInfo(K index);

	Mono<String> aliasAdd(K name, K index);

	Mono<String> aliasUpdate(K name, K index);

	Mono<String> aliasDel(K name);

}
