package com.redislabs.lettusearch;

import io.lettuce.core.api.async.RedisAsyncCommands;

public interface RediSearchAsyncCommands<K, V> extends RedisAsyncCommands<K, V>, IndexAsyncCommands<K, V>,
		SearchAsyncCommands<K, V>, AggregateAsyncCommands<K, V>, SuggestAsyncCommands<K, V> {

	StatefulRediSearchConnection<K, V> getStatefulConnection();
}
