package com.redislabs.lettusearch.search.api.sync;

import java.util.List;
import java.util.Map;

import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.AggregateResults;
import com.redislabs.lettusearch.aggregate.AggregateWithCursorResults;
import com.redislabs.lettusearch.aggregate.CursorOptions;
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

	String drop(String index, DropOptions options);

	String alter(String index, K field, FieldOptions options);

	List<Object> ftInfo(String index);

	String add(String index, K docId, double score, Map<K, V> fields);

	String add(String index, K docId, double score, Map<K, V> fields, AddOptions options);

	String add(String index, K docId, double score, Map<K, V> fields, AddOptions options, V payload);

	Map<K, V> get(String index, K docId);

	List<Map<K, V>> ftMget(String index, @SuppressWarnings("unchecked") K... docIds);

	boolean del(String index, K docId, boolean deleteDoc);

	SearchResults<K, V> search(String index, String query);

	SearchResults<K, V> search(String index, String query, SearchOptions options);

	AggregateResults<K, V> aggregate(String index, String query, AggregateOptions options);

	AggregateWithCursorResults<K, V> aggregate(String index, String query, AggregateOptions options,
			CursorOptions cursorOptions);

	AggregateWithCursorResults<K, V> cursorRead(String index, long cursor);

	AggregateWithCursorResults<K, V> cursorRead(String index, long cursor, long count);

	String cursorDelete(String index, long cursor);

	String aliasAdd(String name, String index);

	String aliasUpdate(String name, String index);

	String aliasDel(String name);

}
