package com.redislabs.lettusearch.search.api.sync;

import java.util.List;
import java.util.Map;

import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.AggregateResults;
import com.redislabs.lettusearch.aggregate.AggregateWithCursorResults;
import com.redislabs.lettusearch.aggregate.CursorOptions;
import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.DropOptions;
import com.redislabs.lettusearch.search.Schema;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.search.field.FieldOptions;

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

	String alter(String index, K field, FieldOptions options);

	List<Object> indexInfo(String index);

	String add(String index, K docId, double score, Map<K, V> fields);

	String add(String index, K docId, double score, Map<K, V> fields, AddOptions options);

	String add(String index, K docId, double score, Map<K, V> fields, AddOptions options, V payload);

	Map<K, V> get(String index, K docId);

	boolean del(String index, K docId, boolean deleteDoc);

	SearchResults<K, V> search(String index, String query);

	SearchResults<K, V> search(String index, String query, SearchOptions options);

	AggregateResults<K, V> aggregate(String index, String query, AggregateOptions options);

	AggregateWithCursorResults<K, V> aggregate(String index, String query, AggregateOptions options,
			CursorOptions cursorOptions);

	AggregateWithCursorResults<K, V> cursorRead(String index, long cursor);

	AggregateWithCursorResults<K, V> cursorRead(String index, long cursor, long count);
	
	String cursorDelete(String index, long cursor);

}
