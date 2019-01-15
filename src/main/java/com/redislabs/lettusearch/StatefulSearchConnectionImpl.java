package com.redislabs.lettusearch;

import java.time.Duration;

import com.redislabs.lettusearch.api.async.SearchAsyncCommands;
import com.redislabs.lettusearch.api.async.SearchAsyncCommandsImpl;
import com.redislabs.lettusearch.api.reactive.SearchReactiveCommands;
import com.redislabs.lettusearch.api.reactive.SearchReactiveCommandsImpl;
import com.redislabs.lettusearch.api.sync.SearchCommands;

import io.lettuce.core.RedisChannelWriter;
import io.lettuce.core.StatefulRedisConnectionImpl;
import io.lettuce.core.codec.RedisCodec;

@SuppressWarnings("unchecked")
public class StatefulSearchConnectionImpl<K, V> extends StatefulRedisConnectionImpl<K, V>
		implements StatefulSearchConnection<K, V> {

	public StatefulSearchConnectionImpl(RedisChannelWriter writer, RedisCodec<K, V> codec, Duration timeout) {
		super(writer, codec, timeout);
	}

	@Override
	public SearchAsyncCommands<K, V> async() {
		return (SearchAsyncCommands<K, V>) async;
	}

	@Override
	public SearchCommands<K, V> sync() {
		return (SearchCommands<K, V>) sync;
	}

	@Override
	public SearchReactiveCommands<K, V> reactive() {
		return (SearchReactiveCommands<K, V>) reactive;
	}

	@Override
	protected SearchAsyncCommandsImpl<K, V> newRedisAsyncCommandsImpl() {
		return new SearchAsyncCommandsImpl<>(this, codec);
	}

	@Override
	protected SearchCommands<K, V> newRedisSyncCommandsImpl() {
		return syncHandler(async(), SearchCommands.class);
	}

	@Override
	protected SearchReactiveCommandsImpl<K, V> newRedisReactiveCommandsImpl() {
		return new SearchReactiveCommandsImpl<>(this, codec);
	}

}
