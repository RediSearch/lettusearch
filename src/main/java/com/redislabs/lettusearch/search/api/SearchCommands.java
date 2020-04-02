package com.redislabs.lettusearch.search.api;

import java.util.List;
import java.util.Map;

import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;

/**
 * Synchronously executed commands for RediSearch search index.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 * @since 1.0
 */
public interface SearchCommands<K, V> {

	String add(String index, Document<K, V> document, AddOptions options);

	boolean del(String index, K docId, boolean deleteDoc);

	Map<K, V> get(String index, K docId);

	List<Map<K, V>> ftMget(String index, K... docIds);

	SearchResults<K, V> search(String index, String query, Object... options);

	SearchResults<K, V> search(String index, String query, SearchOptions options);

}
