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

	RedisFuture<String> add(String index, Document<K, V> document, AddOptions options);

	RedisFuture<Boolean> del(String index, K docId, boolean deleteDoc);

	RedisFuture<Map<K, V>> get(String index, K docId);

	RedisFuture<List<Map<K, V>>> ftMget(String index, @SuppressWarnings("unchecked") K... docIds);

	RedisFuture<SearchResults<K, V>> search(String index, String query, Object... options);

	RedisFuture<SearchResults<K, V>> search(String index, String query, SearchOptions options);

}
