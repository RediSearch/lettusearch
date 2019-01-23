package com.redislabs.lettusearch.search.api.sync;

import java.util.Map;

import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.DropOptions;
import com.redislabs.lettusearch.search.Schema;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;

import io.lettuce.core.api.sync.RedisCommands;

/**
 * Synchronously executed commands for search.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 */
public interface SearchCommands<K, V> extends RedisCommands<K, V> {

	String create(String index, Schema schema);

	String drop(String index, DropOptions options);

	String add(String index, K docId, double score, Map<K, V> fields, AddOptions document);

	SearchResults<K, V> search(String index, String query, SearchOptions options);

}
