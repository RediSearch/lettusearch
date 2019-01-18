package com.redislabs.lettusearch.search.api.async;

import java.util.Map;

import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.DropOptions;
import com.redislabs.lettusearch.search.Schema;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.async.RedisAsyncCommands;

public interface SearchAsyncCommands<K, V> extends RedisAsyncCommands<K, V> {

	RedisFuture<String> create(String index, Schema schema);

	RedisFuture<String> add(String index, K docId, Map<K, V> fields, Double score, AddOptions document);

	RedisFuture<SearchResults<K, V>> search(String index, String query, SearchOptions options);

	RedisFuture<String> drop(String index, DropOptions options);
}