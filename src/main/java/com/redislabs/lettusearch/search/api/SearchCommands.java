package com.redislabs.lettusearch.search.api;

import java.util.List;
import java.util.Map;

import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.CreateOptions;
import com.redislabs.lettusearch.search.DropOptions;
import com.redislabs.lettusearch.search.Schema;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.search.field.FieldOptions;

/**
 * Synchronously executed commands for RediSearch search index.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 * @since 1.0
 */
public interface SearchCommands<K, V> {

	String create(String index, Schema schema);

	String create(String index, Schema schema, CreateOptions options);

	String drop(String index);

	String drop(String index, DropOptions options);

	String alter(String index, K field, FieldOptions options);

	List<Object> indexInfo(String index);

	String aliasAdd(String name, String index);

	String aliasUpdate(String name, String index);

	String aliasDel(String name);

	String add(String index, K docId, double score, Map<K, V> fields);

	String add(String index, K docId, double score, Map<K, V> fields, V payload);

	String add(String index, K docId, double score, Map<K, V> fields, AddOptions options);

	String add(String index, K docId, double score, Map<K, V> fields, AddOptions options, V payload);

	boolean del(String index, K docId, boolean deleteDoc);

	Map<K, V> get(String index, K docId);

	List<Map<K, V>> ftMget(String index, @SuppressWarnings("unchecked") K... docIds);

	SearchResults<K, V> search(String index, String query);

	SearchResults<K, V> search(String index, String query, SearchOptions options);

}
