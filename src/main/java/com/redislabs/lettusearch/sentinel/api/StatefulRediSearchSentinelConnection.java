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
package com.redislabs.lettusearch.sentinel.api;

import com.redislabs.lettusearch.sentinel.api.async.RediSearchSentinelAsyncCommands;
import com.redislabs.lettusearch.sentinel.api.reactive.RediSearchSentinelReactiveCommands;
import com.redislabs.lettusearch.sentinel.api.sync.RediSearchSentinelCommands;

import io.lettuce.core.protocol.ConnectionWatchdog;
import io.lettuce.core.sentinel.api.StatefulRedisSentinelConnection;

/**
 * A thread-safe connection to a redis server. Multiple threads may share one
 * {@link StatefulRediSearchSentinelConnection}.
 *
 * A {@link ConnectionWatchdog} monitors each connection and reconnects
 * automatically until {@link #close} is called. All pending commands will be
 * (re)sent after successful reconnection.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 */
public interface StatefulRediSearchSentinelConnection<K, V> extends StatefulRedisSentinelConnection<K, V> {

	/**
	 * Returns the {@link RediSearchSentinelCommands} API for the current
	 * connection. Does not create a new connection.
	 *
	 * @return the synchronous API for the underlying connection.
	 */
	RediSearchSentinelCommands<K, V> sync();

	/**
	 * Returns the {@link RediSearchSentinelAsyncCommands} API for the current
	 * connection. Does not create a new connection.
	 *
	 * @return the asynchronous API for the underlying connection.
	 */
	RediSearchSentinelAsyncCommands<K, V> async();

	/**
	 * Returns the {@link RediSearchSentinelReactiveCommands} API for the current
	 * connection. Does not create a new connection.
	 *
	 * @return the reactive API for the underlying connection.
	 */
	RediSearchSentinelReactiveCommands<K, V> reactive();
}
