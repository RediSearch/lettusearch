package com.redislabs.lettusearch.search.api.sync;

import com.redislabs.lettusearch.search.Document;
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

	String add(String index, Document document);

	String create(String index, Schema schema);

	String drop(String index, DropOptions options);

	SearchResults<K, V> search(String index, String query, SearchOptions options);

}
