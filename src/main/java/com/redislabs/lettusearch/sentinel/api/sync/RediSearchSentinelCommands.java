package com.redislabs.lettusearch.sentinel.api.sync;

import com.redislabs.lettusearch.sentinel.api.StatefulRediSearchSentinelConnection;

import io.lettuce.core.sentinel.api.sync.RedisSentinelCommands;

public interface RediSearchSentinelCommands<K, V> extends RedisSentinelCommands<K, V> {

	/**
	 * @return the underlying connection.
	 */
	StatefulRediSearchSentinelConnection<K, V> getStatefulConnection();

}
