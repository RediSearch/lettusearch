package com.redislabs.lettusearch;

import static io.lettuce.core.LettuceStrings.isEmpty;
import static io.lettuce.core.LettuceStrings.isNotEmpty;

import java.net.SocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

import com.redislabs.lettusearch.impl.StatefulRediSearchConnectionImpl;
import com.redislabs.lettusearch.impl.StatefulRediSearchSentinelConnectionImpl;
import com.redislabs.lettusearch.sentinel.api.StatefulRediSearchSentinelConnection;

import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.ConnectionBuilder;
import io.lettuce.core.ConnectionFuture;
import io.lettuce.core.LettuceStrings;
import io.lettuce.core.RedisChannelHandler;
import io.lettuce.core.RedisChannelWriter;
import io.lettuce.core.RedisClient;
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

	public StatefulRediSearchConnection<String, String> connect() {
		return connect(newStringStringCodec());
	}

	public <K, V> StatefulRediSearchConnection<K, V> connect(RedisCodec<K, V> codec) {

		checkForRedisURI();

		return getConnection(connectStandaloneAsync(codec, this.redisURI, timeout));
	}

	public StatefulRediSearchConnection<String, String> connect(RedisURI redisURI) {

		assertNotNull(redisURI);

		return getConnection(connectStandaloneAsync(newStringStringCodec(), redisURI, redisURI.getTimeout()));
	}

	public <K, V> StatefulRediSearchConnection<K, V> connect(RedisCodec<K, V> codec, RedisURI redisURI) {

		assertNotNull(redisURI);

		return getConnection(connectStandaloneAsync(codec, redisURI, redisURI.getTimeout()));
	}

	public <K, V> ConnectionFuture<StatefulRediSearchConnection<K, V>> connectAsync(RedisCodec<K, V> codec,
			RedisURI redisURI) {

		assertNotNull(redisURI);

		return transformAsyncConnectionException(connectStandaloneAsync(codec, redisURI, redisURI.getTimeout()));
	}

	private <K, V> ConnectionFuture<StatefulRediSearchConnection<K, V>> connectStandaloneAsync(RedisCodec<K, V> codec,
			RedisURI redisURI, Duration timeout) {

		assertNotNull(codec);
		checkValidRedisURI(redisURI);

		logger.debug("Trying to get a Redis connection for: " + redisURI);

		DefaultEndpoint endpoint = new DefaultEndpoint(clientOptions, clientResources);
		RedisChannelWriter writer = endpoint;

		if (CommandExpiryWriter.isSupported(clientOptions)) {
			writer = new CommandExpiryWriter(writer, clientOptions, clientResources);
		}

		StatefulRediSearchConnectionImpl<K, V> connection = newStatefulRedisConnection(writer, codec, timeout);
		ConnectionFuture<StatefulRediSearchConnection<K, V>> future = connectStatefulAsync(connection, codec, endpoint,
				redisURI, () -> new CommandHandler(clientOptions, clientResources, endpoint));

		future.whenComplete((channelHandler, throwable) -> {

			if (throwable != null) {
				connection.close();
			}
		});

		return future;
	}

	@SuppressWarnings("unchecked")
	private <K, V, S> ConnectionFuture<S> connectStatefulAsync(StatefulRediSearchConnectionImpl<K, V> connection,
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

		ConnectionFuture<?> sync = initializeChannelAsync(connectionBuilder);

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

	protected <K, V> StatefulRediSearchConnectionImpl<K, V> newStatefulRedisConnection(RedisChannelWriter channelWriter,
			RedisCodec<K, V> codec, Duration timeout) {
		return new StatefulRediSearchConnectionImpl<>(channelWriter, codec, timeout);
	}

	protected Mono<SocketAddress> getSocketAddress(RedisURI redisURI) {

		return Mono.defer(() -> {
			if (redisURI.getSentinelMasterId() != null && !redisURI.getSentinels().isEmpty()) {
				logger.debug("Connecting to Redis using Sentinels {}, MasterId {}", redisURI.getSentinels(),
						redisURI.getSentinelMasterId());
				return lookupRedis(redisURI).switchIfEmpty(Mono.error(new RedisConnectionException(
						"Cannot provide redisAddress using sentinel for masterId " + redisURI.getSentinelMasterId())));

			} else {
				return Mono.fromCallable(() -> clientResources.socketAddressResolver().resolve((redisURI)));
			}
		});
	}

	private Mono<SocketAddress> lookupRedis(RedisURI sentinelUri) {

		Mono<StatefulRediSearchSentinelConnection<String, String>> connection = Mono
				.fromCompletionStage(connectSentinelAsync(newStringStringCodec(), sentinelUri, timeout));

		return connection.flatMap(c -> c.reactive() //
				.getMasterAddrByName(sentinelUri.getSentinelMasterId()) //
				.timeout(this.timeout) //
				.flatMap(it -> Mono.fromCompletionStage(c.closeAsync()) //
						.then(Mono.just(it))));
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

	private static <T> CompletableFuture<T> transformAsyncConnectionException(CompletionStage<T> future,
			RedisURI target) {

		return ConnectionFuture.from(null, future.toCompletableFuture()).thenCompose((v, e) -> {

			if (e != null) {
				return Futures.failed(RedisConnectionException.create(target.toString(), e));
			}

			return CompletableFuture.completedFuture(v);
		}).toCompletableFuture();
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

	/**
	 * Open a connection to a Redis Sentinel that treats keys and values as UTF-8
	 * strings.
	 *
	 * @return A new stateful Redis Sentinel connection
	 */
	public StatefulRediSearchSentinelConnection<String, String> connectSentinel() {
		return connectSentinel(newStringStringCodec());
	}

	/**
	 * Open a connection to a Redis Sentinel that treats keys and use the supplied
	 * {@link RedisCodec codec} to encode/decode keys and values. The client
	 * {@link RedisURI} must contain one or more sentinels.
	 *
	 * @param codec Use this codec to encode/decode keys and values, must not be
	 *              {@literal null}
	 * @param <K>   Key type
	 * @param <V>   Value type
	 * @return A new stateful Redis Sentinel connection
	 */
	public <K, V> StatefulRediSearchSentinelConnection<K, V> connectSentinel(RedisCodec<K, V> codec) {
		checkForRedisURI();
		return getConnection(connectSentinelAsync(codec, redisURI, timeout));
	}

	/**
	 * Open a connection to a Redis Sentinel using the supplied {@link RedisURI}
	 * that treats keys and values as UTF-8 strings. The client {@link RedisURI}
	 * must contain one or more sentinels.
	 *
	 * @param redisURI the Redis server to connect to, must not be {@literal null}
	 * @return A new connection
	 */
	public StatefulRediSearchSentinelConnection<String, String> connectSentinel(RedisURI redisURI) {

		assertNotNull(redisURI);

		return getConnection(connectSentinelAsync(newStringStringCodec(), redisURI, redisURI.getTimeout()));
	}

	/**
	 * Open a connection to a Redis Sentinel using the supplied {@link RedisURI} and
	 * use the supplied {@link RedisCodec codec} to encode/decode keys and values.
	 * The client {@link RedisURI} must contain one or more sentinels.
	 *
	 * @param codec    the Redis server to connect to, must not be {@literal null}
	 * @param redisURI the Redis server to connect to, must not be {@literal null}
	 * @param <K>      Key type
	 * @param <V>      Value type
	 * @return A new connection
	 */
	public <K, V> StatefulRediSearchSentinelConnection<K, V> connectSentinel(RedisCodec<K, V> codec,
			RedisURI redisURI) {

		assertNotNull(redisURI);

		return getConnection(connectSentinelAsync(codec, redisURI, redisURI.getTimeout()));
	}

	/**
	 * Open asynchronously a connection to a Redis Sentinel using the supplied
	 * {@link RedisURI} and use the supplied {@link RedisCodec codec} to
	 * encode/decode keys and values. The client {@link RedisURI} must contain one
	 * or more sentinels.
	 *
	 * @param codec    the Redis server to connect to, must not be {@literal null}
	 * @param redisURI the Redis server to connect to, must not be {@literal null}
	 * @param <K>      Key type
	 * @param <V>      Value type
	 * @return A new connection
	 * @since 5.1
	 */
	public <K, V> CompletableFuture<StatefulRediSearchSentinelConnection<K, V>> connectSentinelAsync(
			RedisCodec<K, V> codec, RedisURI redisURI) {

		assertNotNull(redisURI);

		return transformAsyncConnectionException(connectSentinelAsync(codec, redisURI, redisURI.getTimeout()),
				redisURI);
	}

	private <K, V> CompletableFuture<StatefulRediSearchSentinelConnection<K, V>> connectSentinelAsync(
			RedisCodec<K, V> codec, RedisURI redisURI, Duration timeout) {

		assertNotNull(codec);
		checkValidRedisURI(redisURI);

		ConnectionBuilder connectionBuilder = ConnectionBuilder.connectionBuilder();
		connectionBuilder.clientOptions(ClientOptions.copyOf(getOptions()));
		connectionBuilder.clientResources(clientResources);

		DefaultEndpoint endpoint = new DefaultEndpoint(clientOptions, clientResources);
		RedisChannelWriter writer = endpoint;

		if (CommandExpiryWriter.isSupported(clientOptions)) {
			writer = new CommandExpiryWriter(writer, clientOptions, clientResources);
		}

		StatefulRediSearchSentinelConnectionImpl<K, V> connection = newStatefulRediSearchSentinelConnection(writer,
				codec, timeout);

		logger.debug("Trying to get a Redis Sentinel connection for one of: " + redisURI.getSentinels());

		connectionBuilder.endpoint(endpoint)
				.commandHandler(() -> new CommandHandler(clientOptions, clientResources, endpoint))
				.connection(connection);
		connectionBuilder(getSocketAddressSupplier(redisURI), connectionBuilder, redisURI);

		if (clientOptions.isPingBeforeActivateConnection()) {
			connectionBuilder.enablePingBeforeConnect();
		}

		Mono<StatefulRediSearchSentinelConnection<K, V>> connect;
		if (redisURI.getSentinels().isEmpty() && (isNotEmpty(redisURI.getHost()) || !isEmpty(redisURI.getSocket()))) {

			channelType(connectionBuilder, redisURI);
			connect = Mono.fromCompletionStage(initializeChannelAsync(connectionBuilder));
		} else {

			List<RedisURI> sentinels = redisURI.getSentinels();
			validateUrisAreOfSameConnectionType(sentinels);

			Mono<StatefulRediSearchSentinelConnection<K, V>> connectionLoop = Mono.defer(() -> {

				RedisURI uri = sentinels.get(0);
				channelType(connectionBuilder, uri);
				return connectSentinel(connectionBuilder, uri);
			});

			for (int i = 1; i < sentinels.size(); i++) {

				RedisURI uri = sentinels.get(i);
				connectionLoop = connectionLoop.onErrorResume(t -> connectSentinel(connectionBuilder, uri));
			}

			connect = connectionLoop;
		}

		if (LettuceStrings.isNotEmpty(redisURI.getClientName())) {
			connect = connect.doOnNext(c -> connection.setClientName(redisURI.getClientName()));
		}

		return connect.doOnError(e -> {

			connection.close();
			throw new RedisConnectionException("Cannot connect to a Redis Sentinel: " + redisURI.getSentinels(), e);
		}).toFuture();
	}

	private static void validateUrisAreOfSameConnectionType(List<RedisURI> redisUris) {

		boolean unixDomainSocket = false;
		boolean inetSocket = false;
		for (RedisURI sentinel : redisUris) {
			if (sentinel.getSocket() != null) {
				unixDomainSocket = true;
			}
			if (sentinel.getHost() != null) {
				inetSocket = true;
			}
		}

		if (unixDomainSocket && inetSocket) {
			throw new RedisConnectionException("You cannot mix unix domain socket and IP socket URI's");
		}
	}

	private <K, V> Mono<StatefulRediSearchSentinelConnection<K, V>> connectSentinel(ConnectionBuilder connectionBuilder,
			RedisURI uri) {

		connectionBuilder.socketAddressSupplier(getSocketAddressSupplier(uri));
		SocketAddress socketAddress = clientResources.socketAddressResolver().resolve(uri);
		logger.debug("Connecting to Redis Sentinel, address: " + socketAddress);

		Mono<StatefulRediSearchSentinelConnection<K, V>> connectionMono = Mono
				.fromCompletionStage(initializeChannelAsync(connectionBuilder));

		return connectionMono.onErrorMap(CompletionException.class, Throwable::getCause) //
				.doOnError(t -> logger.warn("Cannot connect Redis Sentinel at " + uri + ": " + t.toString())) //
				.onErrorMap(e -> new RedisConnectionException("Cannot connect Redis Sentinel at " + uri, e));
	}

	/**
	 * Create a new instance of {@link StatefulRediSearchSentinelConnectionImpl} or
	 * a subclass.
	 * <p>
	 * Subclasses of {@link RedisClient} may override that method.
	 *
	 * @param channelWriter the channel writer
	 * @param codec         codec
	 * @param timeout       default timeout
	 * @param <K>           Key-Type
	 * @param <V>           Value Type
	 * @return new instance of StatefulRediSearchSentinelConnectionImpl
	 */
	protected <K, V> StatefulRediSearchSentinelConnectionImpl<K, V> newStatefulRediSearchSentinelConnection(
			RedisChannelWriter channelWriter, RedisCodec<K, V> codec, Duration timeout) {
		return new StatefulRediSearchSentinelConnectionImpl<>(channelWriter, codec, timeout);
	}

}
