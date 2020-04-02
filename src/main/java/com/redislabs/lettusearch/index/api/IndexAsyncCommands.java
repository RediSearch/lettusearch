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

	RedisFuture<String> create(String index, Schema schema, CreateOptions options);

	RedisFuture<String> drop(String index, DropOptions options);

	RedisFuture<String> alter(String index, K field, FieldOptions options);

	RedisFuture<List<Object>> ftInfo(String index);

	RedisFuture<String> aliasAdd(String name, String index);

	RedisFuture<String> aliasUpdate(String name, String index);

	RedisFuture<String> aliasDel(String name);

}
