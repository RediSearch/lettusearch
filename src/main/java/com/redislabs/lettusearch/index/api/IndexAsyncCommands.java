package com.redislabs.lettusearch.index.api;

import java.util.List;

import com.redislabs.lettusearch.index.CreateOptions;
import com.redislabs.lettusearch.index.DropOptions;
import com.redislabs.lettusearch.index.Schema;
import com.redislabs.lettusearch.index.field.FieldOptions;

import io.lettuce.core.RedisFuture;

/**
 * Asynchronously-executded index administration commands for RediSearch.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 * @since 1.0
 */
public interface IndexAsyncCommands<K, V> {

	RedisFuture<String> create(K index, Schema schema);

	RedisFuture<String> create(K index, Schema schema, CreateOptions options);

	RedisFuture<String> drop(K index);

	RedisFuture<String> drop(K index, DropOptions options);

	RedisFuture<String> alter(K index, K field, FieldOptions options);

	RedisFuture<List<Object>> ftInfo(K index);

	RedisFuture<String> aliasAdd(K name, K index);

	RedisFuture<String> aliasUpdate(K name, K index);

	RedisFuture<String> aliasDel(K name);

}
