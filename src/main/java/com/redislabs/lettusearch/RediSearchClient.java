/*
 * Copyright 2011-2018 the original author or authors.
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
package com.redislabs.lettusearch;

import static io.lettuce.core.LettuceStrings.isEmpty;

import java.net.SocketAddress;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.ConnectionBuilder;
import io.lettuce.core.ConnectionFuture;
import io.lettuce.core.LettuceStrings;
import io.lettuce.core.RedisChannelHandler;
import io.lettuce.core.RedisChannelWriter;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.RedisURI;
import io.lettuce.core.SslConnectionBuilder;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.internal.Futures;
import io.lettuce.core.internal.LettuceAssert;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.CommandArgs;
import io.lettuce.core.protocol.CommandExpiryWriter;
import io.lettuce.core.protocol.CommandHandler;
import io.lettuce.core.protocol.CommandType;
import io.lettuce.core.protocol.DefaultEndpoint;
import io.lettuce.core.protocol.Endpoint;
import io.lettuce.core.resource.ClientResources;
import reactor.core.publisher.Mono;

public class RediSearchClient extends AbstractRedisClient {

	private static final RedisURI EMPTY_URI = new RedisURI();

	private final RedisURI redisURI;

	protected RediSearchClient(ClientResources clientResources, RedisURI redisURI) {

		super(clientResources);

		assertNotNull(redisURI);

		this.redisURI = redisURI;
		setDefaultTimeout(redisURI.getTimeout());
	}

	protected RediSearchClient() {
		this(null, EMPTY_URI);
	}

	public static RediSearchClient create() {
		return new RediSearchClient(null, EMPTY_URI);
	}

	public static RediSearchClient create(RedisURI redisURI) {
		assertNotNull(redisURI);
		return new RediSearchClient(null, redisURI);
	}

	public static RediSearchClient create(String uri) {
		LettuceAssert.notEmpty(uri, "URI must not be empty");
		return new RediSearchClient(null, RedisURI.create(uri));
	}

	public static RediSearchClient create(ClientResources clientResources) {
		assertNotNull(clientResources);
		return new RediSearchClient(clientResources, EMPTY_URI);
	}

	public static RediSearchClient create(ClientResources clientResources, String uri) {
		assertNotNull(clientResources);
		LettuceAssert.notEmpty(uri, "URI must not be empty");
		return create(clientResources, RedisURI.create(uri));
	}

	public static RediSearchClient create(ClientResources clientResources, RedisURI redisURI) {
		assertNotNull(clientResources);
		assertNotNull(redisURI);
		return new RediSearchClient(clientResources, redisURI);
	}

	public StatefulSearchConnection<String, String> connect() {
		return connect(newStringStringCodec());
	}

	public <K, V> StatefulSearchConnection<K, V> connect(RedisCodec<K, V> codec) {

		checkForRedisURI();

		return getConnection(connectStandaloneAsync(codec, this.redisURI, timeout));
	}

	public StatefulSearchConnection<String, String> connect(RedisURI redisURI) {

		assertNotNull(redisURI);

		return getConnection(connectStandaloneAsync(newStringStringCodec(), redisURI, redisURI.getTimeout()));
	}

	public <K, V> StatefulSearchConnection<K, V> connect(RedisCodec<K, V> codec, RedisURI redisURI) {

		assertNotNull(redisURI);

		return getConnection(connectStandaloneAsync(codec, redisURI, redisURI.getTimeout()));
	}

	public <K, V> ConnectionFuture<StatefulSearchConnection<K, V>> connectAsync(RedisCodec<K, V> codec,
			RedisURI redisURI) {

		assertNotNull(redisURI);

		return transformAsyncConnectionException(connectStandaloneAsync(codec, redisURI, redisURI.getTimeout()));
	}

	private <K, V> ConnectionFuture<StatefulSearchConnection<K, V>> connectStandaloneAsync(RedisCodec<K, V> codec,
			RedisURI redisURI, Duration timeout) {

		assertNotNull(codec);
		checkValidRedisURI(redisURI);

		logger.debug("Trying to get a Redis connection for: " + redisURI);

		DefaultEndpoint endpoint = new DefaultEndpoint(clientOptions, clientResources);
		RedisChannelWriter writer = endpoint;

		if (CommandExpiryWriter.isSupported(clientOptions)) {
			writer = new CommandExpiryWriter(writer, clientOptions, clientResources);
		}

		StatefulSearchConnectionImpl<K, V> connection = newStatefulRedisConnection(writer, codec, timeout);
		ConnectionFuture<StatefulSearchConnection<K, V>> future = connectStatefulAsync(connection, codec, endpoint,
				redisURI, () -> new CommandHandler(clientOptions, clientResources, endpoint));

		future.whenComplete((channelHandler, throwable) -> {

			if (throwable != null) {
				connection.close();
			}
		});

		return future;
	}

	@SuppressWarnings("unchecked")
	private <K, V, S> ConnectionFuture<S> connectStatefulAsync(StatefulSearchConnectionImpl<K, V> connection,
			RedisCodec<K, V> codec, Endpoint endpoint, RedisURI redisURI,
			Supplier<CommandHandler> commandHandlerSupplier) {

		ConnectionBuilder connectionBuilder;
		if (redisURI.isSsl()) {
			SslConnectionBuilder sslConnectionBuilder = SslConnectionBuilder.sslConnectionBuilder();
			sslConnectionBuilder.ssl(redisURI);
			connectionBuilder = sslConnectionBuilder;
		} else {
			connectionBuilder = ConnectionBuilder.connectionBuilder();
		}

		connectionBuilder.connection(connection);
		connectionBuilder.clientOptions(clientOptions);
		connectionBuilder.clientResources(clientResources);
		connectionBuilder.commandHandler(commandHandlerSupplier).endpoint(endpoint);

		connectionBuilder(getSocketAddressSupplier(redisURI), connectionBuilder, redisURI);
		channelType(connectionBuilder, redisURI);

		if (clientOptions.isPingBeforeActivateConnection()) {
			if (hasPassword(redisURI)) {
				connectionBuilder.enableAuthPingBeforeConnect();
			} else {
				connectionBuilder.enablePingBeforeConnect();
			}
		}

		ConnectionFuture<RedisChannelHandler<K, V>> future = initializeChannelAsync(connectionBuilder);
		ConnectionFuture<?> sync = future;

		if (!clientOptions.isPingBeforeActivateConnection() && hasPassword(redisURI)) {

			sync = sync.thenCompose(channelHandler -> {

				CommandArgs<K, V> args = new CommandArgs<>(codec).add(redisURI.getPassword());
				return connection.async().dispatch(CommandType.AUTH, new StatusOutput<>(codec), args);
			});
		}

		if (LettuceStrings.isNotEmpty(redisURI.getClientName())) {
			sync = sync.thenApply(channelHandler -> {
				connection.setClientName(redisURI.getClientName());
				return channelHandler;
			});
		}

		if (redisURI.getDatabase() != 0) {

			sync = sync.thenCompose(channelHandler -> {

				CommandArgs<K, V> args = new CommandArgs<>(codec).add(redisURI.getDatabase());
				return connection.async().dispatch(CommandType.SELECT, new StatusOutput<>(codec), args);
			});
		}

		return sync.thenApply(channelHandler -> (S) connection);
	}

	private static boolean hasPassword(RedisURI redisURI) {
		return redisURI.getPassword() != null && redisURI.getPassword().length != 0;
	}

	@Override
	public void setOptions(ClientOptions clientOptions) {
		super.setOptions(clientOptions);
	}

	public ClientResources getResources() {
		return clientResources;
	}

	protected <K, V> StatefulSearchConnectionImpl<K, V> newStatefulRedisConnection(RedisChannelWriter channelWriter,
			RedisCodec<K, V> codec, Duration timeout) {
		return new StatefulSearchConnectionImpl<>(channelWriter, codec, timeout);
	}

	protected Mono<SocketAddress> getSocketAddress(RedisURI redisURI) {

		return Mono.defer(() -> {
			return Mono.fromCallable(() -> clientResources.socketAddressResolver().resolve((redisURI)));
		});
	}

	protected RedisCodec<String, String> newStringStringCodec() {
		return StringCodec.UTF8;
	}

	private Mono<SocketAddress> getSocketAddressSupplier(RedisURI redisURI) {
		return getSocketAddress(redisURI)
				.doOnNext(addr -> logger.debug("Resolved SocketAddress {} using {}", addr, redisURI));
	}

	private static <T> ConnectionFuture<T> transformAsyncConnectionException(ConnectionFuture<T> future) {

		return future.thenCompose((v, e) -> {

			if (e != null) {
				return Futures.failed(RedisConnectionException.create(future.getRemoteAddress(), e));
			}

			return CompletableFuture.completedFuture(v);
		});
	}

	private static void checkValidRedisURI(RedisURI redisURI) {

		LettuceAssert.notNull(redisURI, "A valid RedisURI is required");

		if (redisURI.getSentinels().isEmpty()) {
			if (isEmpty(redisURI.getHost()) && isEmpty(redisURI.getSocket())) {
				throw new IllegalArgumentException("RedisURI for Redis Standalone does not contain a host or a socket");
			}
		} else {

			if (isEmpty(redisURI.getSentinelMasterId())) {
				throw new IllegalArgumentException("TRedisURI for Redis Sentinel requires a masterId");
			}

			for (RedisURI sentinel : redisURI.getSentinels()) {
				if (isEmpty(sentinel.getHost()) && isEmpty(sentinel.getSocket())) {
					throw new IllegalArgumentException(
							"RedisURI for Redis Sentinel does not contain a host or a socket");
				}
			}
		}
	}

	private static <K, V> void assertNotNull(RedisCodec<K, V> codec) {
		LettuceAssert.notNull(codec, "RedisCodec must not be null");
	}

	private static void assertNotNull(RedisURI redisURI) {
		LettuceAssert.notNull(redisURI, "RedisURI must not be null");
	}

	private static void assertNotNull(ClientResources clientResources) {
		LettuceAssert.notNull(clientResources, "ClientResources must not be null");
	}

	private void checkForRedisURI() {
		LettuceAssert.assertState(this.redisURI != EMPTY_URI,
				"RedisURI is not available. Use RedisClient(Host), RedisClient(Host, Port) or RedisClient(RedisURI) to construct your client.");
		checkValidRedisURI(this.redisURI);
	}
}
