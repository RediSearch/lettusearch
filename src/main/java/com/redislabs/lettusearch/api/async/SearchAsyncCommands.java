package com.redislabs.lettusearch.api.async;

import com.redislabs.lettusearch.StatefulSearchConnection;
import com.redislabs.lettusearch.api.Document;
import com.redislabs.lettusearch.api.DropOptions;
import com.redislabs.lettusearch.api.Schema;
import com.redislabs.lettusearch.api.Suggestion;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.async.RedisAsyncCommands;

public interface SearchAsyncCommands<K, V> extends RedisAsyncCommands<K, V> {

	StatefulSearchConnection<K, V> getStatefulConnection();

	RedisFuture<String> add(String index, Document document);

	RedisFuture<String> create(String index, Schema schema);

	RedisFuture<String> drop(String index, DropOptions options);

	RedisFuture<Long> add(String key, Suggestion suggestion);

}