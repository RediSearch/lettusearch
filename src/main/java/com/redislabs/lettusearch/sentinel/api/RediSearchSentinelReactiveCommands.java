package com.redislabs.lettusearch.sentinel.api;

import io.lettuce.core.sentinel.api.reactive.RedisSentinelReactiveCommands;

public interface RediSearchSentinelReactiveCommands<K, V> extends RedisSentinelReactiveCommands<K, V> {

	@Override
	StatefulRediSearchSentinelConnection<K, V> getStatefulConnection();

}
