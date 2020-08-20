package com.redislabs.lettusearch.index.api;

import java.util.List;
import java.util.Map;

import com.redislabs.lettusearch.index.CreateOptions;
import com.redislabs.lettusearch.index.DropOptions;
import com.redislabs.lettusearch.index.Schema;
import com.redislabs.lettusearch.index.field.FieldOptions;
import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.Document;

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

	RedisFuture<String> create(K index, Schema<K> schema);

	RedisFuture<String> create(K index, Schema<K> schema, CreateOptions<K, V> options);

	RedisFuture<String> drop(K index);

	RedisFuture<String> drop(K index, DropOptions options);

	RedisFuture<String> alter(K index, K field, FieldOptions options);

	RedisFuture<List<Object>> ftInfo(K index);

	RedisFuture<String> aliasAdd(K name, K index);

	RedisFuture<String> aliasUpdate(K name, K index);

	RedisFuture<String> aliasDel(K name);

	RedisFuture<String> add(K index, Document<K, V> document);

	RedisFuture<String> add(K index, Document<K, V> document, AddOptions options);

	RedisFuture<Boolean> del(K index, K docId);

	RedisFuture<Boolean> del(K index, K docId, boolean deleteDoc);

	RedisFuture<Map<K, V>> get(K index, K docId);

	@SuppressWarnings("unchecked")
	RedisFuture<List<Map<K, V>>> ftMget(K index, K... docIds);

}
