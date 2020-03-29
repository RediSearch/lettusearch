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

	Mono<String> create(String index, Schema schema);

	Mono<String> create(String index, Schema schema, CreateOptions options);

	Mono<String> drop(String index);

	Mono<String> drop(String index, DropOptions options);

	Mono<String> alter(String index, K field, FieldOptions options);

	Flux<Object> ftInfo(String index);

	Mono<String> aliasAdd(String name, String index);

	Mono<String> aliasUpdate(String name, String index);

	Mono<String> aliasDel(String name);

}
