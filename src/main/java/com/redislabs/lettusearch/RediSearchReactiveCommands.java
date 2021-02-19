package com.redislabs.lettusearch;

import io.lettuce.core.api.reactive.RedisReactiveCommands;

public interface RediSearchReactiveCommands<K, V> extends RedisReactiveCommands<K, V>, IndexReactiveCommands<K, V>,
		SearchReactiveCommands<K, V>, AggregateReactiveCommands<K, V>, SuggestReactiveCommands<K, V> {

	StatefulRediSearchConnection<K, V> getStatefulConnection();
}
