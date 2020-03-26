package com.redislabs.lettusearch.search.api;

import java.util.List;
import java.util.Map;

import com.redislabs.lettusearch.search.AddArgs;
import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.DelArgs;
import com.redislabs.lettusearch.search.SearchArgs;
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

	String add(String index, AddArgs<K, V> args);

	String add(String index, K docId, double score, Map<K, V> fields, V payload, AddOptions options);

	boolean del(String index, DelArgs<K> args);

	boolean del(String index, K docId, boolean deleteDoc);

	Map<K, V> get(String index, K docId);

	List<Map<K, V>> ftMget(String index, @SuppressWarnings("unchecked") K... docIds);

	SearchResults<K, V> search(String index, String query);

	SearchResults<K, V> search(String index, SearchArgs args);

}
