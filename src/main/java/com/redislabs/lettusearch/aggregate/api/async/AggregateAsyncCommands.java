package com.redislabs.lettusearch.aggregate.api.async;

import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.AggregateResults;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.async.RedisAsyncCommands;

public interface AggregateAsyncCommands<K, V> extends RedisAsyncCommands<K, V> {

	RedisFuture<AggregateResults<K, V>> aggregate(String index, String query, AggregateOptions options);
}
