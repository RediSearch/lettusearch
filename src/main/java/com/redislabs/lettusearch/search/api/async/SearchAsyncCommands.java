package com.redislabs.lettusearch.search.api.async;

import java.util.Map;

import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.AggregateResults;
import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.DropOptions;
import com.redislabs.lettusearch.search.Schema;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.async.RedisAsyncCommands;

public interface SearchAsyncCommands<K, V> extends RedisAsyncCommands<K, V> {

	RedisFuture<String> create(String index, Schema schema);

	RedisFuture<String> drop(String index, DropOptions options);

	RedisFuture<String> add(String index, K docId, double score, Map<K, V> fields, AddOptions document);

	RedisFuture<Map<K, V>> get(String index, K docId);

	RedisFuture<Boolean> del(String index, K docId, boolean deleteDoc);

	RedisFuture<SearchResults<K, V>> search(String index, String query, SearchOptions options);

	RedisFuture<AggregateResults<K, V>> aggregate(String index, String query, AggregateOptions options);
}
