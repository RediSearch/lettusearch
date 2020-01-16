/*
 * Copyright 2011-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redislabs.lettusearch.impl;

import java.time.Duration;

import com.redislabs.lettusearch.sentinel.api.RediSearchSentinelAsyncCommands;
import com.redislabs.lettusearch.sentinel.api.RediSearchSentinelCommands;
import com.redislabs.lettusearch.sentinel.api.RediSearchSentinelReactiveCommands;
import com.redislabs.lettusearch.sentinel.api.StatefulRediSearchSentinelConnection;

import io.lettuce.core.RedisChannelWriter;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.sentinel.StatefulRedisSentinelConnectionImpl;

/**
 * @author Julien Ruaux
 */
public class StatefulRediSearchSentinelConnectionImpl<K, V> extends StatefulRedisSentinelConnectionImpl<K, V>
		implements StatefulRediSearchSentinelConnection<K, V> {

	protected final RediSearchSentinelCommands<K, V> sync;
	protected final RediSearchSentinelAsyncCommands<K, V> async;
	protected final RediSearchSentinelReactiveCommands<K, V> reactive;

	public StatefulRediSearchSentinelConnectionImpl(RedisChannelWriter writer, RedisCodec<K, V> codec,
			Duration timeout) {
		super(writer, codec, timeout);
		this.async = new RediSearchSentinelAsyncCommandsImpl<>(this, codec);
		this.sync = syncHandler(async, RediSearchSentinelCommands.class);
		this.reactive = new RediSearchSentinelReactiveCommandsImpl<K, V>(this, codec);
	}

	@Override
	public RediSearchSentinelCommands<K, V> sync() {
		return sync;
	}

	@Override
	public RediSearchSentinelAsyncCommands<K, V> async() {
		return async;
	}

	@Override
	public RediSearchSentinelReactiveCommands<K, V> reactive() {
		return reactive;
	}

}
