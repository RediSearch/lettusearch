package com.redislabs.lettusearch.impl;

import java.time.Duration;

import com.redislabs.lettusearch.RediSearchAsyncCommands;
import com.redislabs.lettusearch.RediSearchCommands;
import com.redislabs.lettusearch.RediSearchReactiveCommands;
import com.redislabs.lettusearch.StatefulRediSearchConnection;

import io.lettuce.core.RedisChannelWriter;
import io.lettuce.core.StatefulRedisConnectionImpl;
import io.lettuce.core.codec.RedisCodec;

@SuppressWarnings("unchecked")
public class StatefulRediSearchConnectionImpl<K, V> extends StatefulRedisConnectionImpl<K, V>
		implements StatefulRediSearchConnection<K, V> {

	public StatefulRediSearchConnectionImpl(RedisChannelWriter writer, RedisCodec<K, V> codec, Duration timeout) {
		super(writer, codec, timeout);
	}

	@Override
	public RediSearchAsyncCommands<K, V> async() {
		return (RediSearchAsyncCommands<K, V>) async;
	}

	@Override
	public RediSearchCommands<K, V> sync() {
		return (RediSearchCommands<K, V>) sync;
	}

	@Override
	public RediSearchReactiveCommands<K, V> reactive() {
		return (RediSearchReactiveCommands<K, V>) reactive;
	}

	@Override
	protected RediSearchAsyncCommandsImpl<K, V> newRedisAsyncCommandsImpl() {
		return new RediSearchAsyncCommandsImpl<>(this, codec);
	}

	@Override
	protected RediSearchCommands<K, V> newRedisSyncCommandsImpl() {
		return syncHandler(async(), RediSearchCommands.class);
	}

	@Override
	protected RediSearchReactiveCommandsImpl<K, V> newRedisReactiveCommandsImpl() {
		return new RediSearchReactiveCommandsImpl<>(this, codec);
	}

}
