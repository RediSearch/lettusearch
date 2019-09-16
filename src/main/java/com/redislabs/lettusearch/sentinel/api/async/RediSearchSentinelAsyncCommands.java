package com.redislabs.lettusearch.sentinel.api.async;

import com.redislabs.lettusearch.sentinel.api.StatefulRediSearchSentinelConnection;

import io.lettuce.core.sentinel.api.async.RedisSentinelAsyncCommands;

public interface RediSearchSentinelAsyncCommands<K, V> extends RedisSentinelAsyncCommands<K, V> {

	@Override
	StatefulRediSearchSentinelConnection<K, V> getStatefulConnection();

}
