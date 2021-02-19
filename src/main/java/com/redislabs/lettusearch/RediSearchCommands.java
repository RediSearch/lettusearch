package com.redislabs.lettusearch;

import io.lettuce.core.api.sync.RedisCommands;

public interface RediSearchCommands<K, V> extends RedisCommands<K, V>, IndexCommands<K, V>, SearchCommands<K, V>,
		AggregateCommands<K, V>, SuggestCommands<K, V> {

	StatefulRediSearchConnection<K, V> getStatefulConnection();
}
