package com.redislabs.lettusearch.sentinel;

import com.redislabs.lettusearch.sentinel.api.StatefulRediSearchSentinelConnection;
import com.redislabs.lettusearch.sentinel.api.async.RediSearchSentinelAsyncCommands;

import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.sentinel.RedisSentinelAsyncCommandsImpl;

public class RediSearchSentinelAsyncCommandsImpl<K, V> extends RedisSentinelAsyncCommandsImpl<K, V>
		implements RediSearchSentinelAsyncCommands<K, V> {

	public RediSearchSentinelAsyncCommandsImpl(StatefulConnection<K, V> connection, RedisCodec<K, V> codec) {
		super(connection, codec);
	}

	@Override
	public StatefulRediSearchSentinelConnection<K, V> getStatefulConnection() {
		return (StatefulRediSearchSentinelConnection<K, V>) super.getStatefulConnection();
	}

}
