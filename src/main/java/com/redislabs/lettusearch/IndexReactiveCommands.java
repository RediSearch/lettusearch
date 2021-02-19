package com.redislabs.lettusearch;

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

    Mono<String> create(K index, Field<K>... fields);

    Mono<String> create(K index, CreateOptions<K, V> options, Field<K>... fields);

    Mono<String> dropIndex(K index);

    Mono<String> dropIndex(K index, boolean deleteDocs);

    Mono<String> alter(K index, Field<K> field);

    Flux<Object> ftInfo(K index);

    Mono<String> aliasAdd(K name, K index);

    Mono<String> aliasUpdate(K name, K index);

    Mono<String> aliasDel(K name);

    Flux<K> list();

}
