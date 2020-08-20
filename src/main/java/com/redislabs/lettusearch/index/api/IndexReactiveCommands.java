package com.redislabs.lettusearch.index.api;

import java.util.Map;

import com.redislabs.lettusearch.index.CreateOptions;
import com.redislabs.lettusearch.index.DropOptions;
import com.redislabs.lettusearch.index.Schema;
import com.redislabs.lettusearch.index.field.FieldOptions;
import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.Document;

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

	Mono<String> create(K index, Schema<K> schema);

	Mono<String> create(K index, Schema<K> schema, CreateOptions<K, V> options);

	Mono<String> drop(K index);

	Mono<String> drop(K index, DropOptions options);

	Mono<String> alter(K index, K field, FieldOptions options);

	Flux<Object> ftInfo(K index);

	Mono<String> aliasAdd(K name, K index);

	Mono<String> aliasUpdate(K name, K index);

	Mono<String> aliasDel(K name);

	Mono<String> add(K index, Document<K, V> document);

	Mono<String> add(K index, Document<K, V> document, AddOptions options);

	Mono<Boolean> del(K index, K docId);

	Mono<Boolean> del(K index, K docId, boolean deleteDoc);

	Mono<Map<K, V>> get(K index, K docId);

	@SuppressWarnings("unchecked")
	Flux<Map<K, V>> ftMget(K index, K... docIds);

}
