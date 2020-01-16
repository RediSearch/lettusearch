package com.redislabs.lettusearch.impl;

import com.redislabs.lettusearch.sentinel.api.RediSearchSentinelReactiveCommands;
import com.redislabs.lettusearch.sentinel.api.StatefulRediSearchSentinelConnection;

import io.lettuce.core.api.StatefulConnection;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.sentinel.RedisSentinelReactiveCommandsImpl;

public class RediSearchSentinelReactiveCommandsImpl<K, V> extends RedisSentinelReactiveCommandsImpl<K, V>
		implements RediSearchSentinelReactiveCommands<K, V> {

	public RediSearchSentinelReactiveCommandsImpl(StatefulConnection<K, V> connection, RedisCodec<K, V> codec) {
		super(connection, codec);
	}

	@Override
	public StatefulRediSearchSentinelConnection<K, V> getStatefulConnection() {
		return (StatefulRediSearchSentinelConnection<K, V>) super.getStatefulConnection();
	}

}
