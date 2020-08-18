package com.redislabs.lettusearch.search.api;

import java.util.List;
import java.util.Map;

import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;

import io.lettuce.core.RedisFuture;

/**
 * Asynchronously executed commands for RediSearch search index.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 * @since 1.0
 */
public interface SearchAsyncCommands<K, V> {

	RedisFuture<String> add(K index, Document<K, V> document);

	RedisFuture<String> add(K index, Document<K, V> document, AddOptions options);

	RedisFuture<Boolean> del(K index, K docId);

	RedisFuture<Boolean> del(K index, K docId, boolean deleteDoc);

	RedisFuture<Map<K, V>> get(K index, K docId);

	@SuppressWarnings("unchecked")
	RedisFuture<List<Map<K, V>>> ftMget(K index, K... docIds);

	RedisFuture<SearchResults<K, V>> search(K index, V query);

	RedisFuture<SearchResults<K, V>> search(K index, V query, Object... options);

	RedisFuture<SearchResults<K, V>> search(K index, V query, SearchOptions options);

}
