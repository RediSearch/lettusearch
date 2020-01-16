package com.redislabs.lettusearch.index.api;

import java.util.List;

import com.redislabs.lettusearch.search.CreateOptions;
import com.redislabs.lettusearch.search.DropOptions;
import com.redislabs.lettusearch.search.Schema;
import com.redislabs.lettusearch.search.field.FieldOptions;

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

	RedisFuture<String> create(String index, Schema schema);

	RedisFuture<String> create(String index, Schema schema, CreateOptions options);

	RedisFuture<String> drop(String index);

	RedisFuture<String> drop(String index, DropOptions options);

	RedisFuture<String> alter(String index, K field, FieldOptions options);

	RedisFuture<List<Object>> indexInfo(String index);

	RedisFuture<String> aliasAdd(String name, String index);

	RedisFuture<String> aliasUpdate(String name, String index);

	RedisFuture<String> aliasDel(String name);

}
