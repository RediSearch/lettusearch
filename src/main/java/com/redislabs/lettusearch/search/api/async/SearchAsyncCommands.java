package com.redislabs.lettusearch.search.api.async;

import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.search.DropOptions;
import com.redislabs.lettusearch.search.Schema;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.async.RedisAsyncCommands;

public interface SearchAsyncCommands<K, V> extends RedisAsyncCommands<K, V> {

	RedisFuture<String> add(String index, Document document);

	RedisFuture<String> create(String index, Schema schema);

	RedisFuture<String> drop(String index, DropOptions options);

	RedisFuture<SearchResults<K, V>> search(String index, String query, SearchOptions options);

}