package com.redislabs.lettusearch;

import static io.lettuce.core.internal.LettuceStrings.isEmpty;

import java.net.SocketAddress;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import com.redislabs.lettusearch.impl.StatefulRediSearchConnectionImpl;

import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.ConnectionBuilder;
import io.lettuce.core.ConnectionFuture;
import io.lettuce.core.RedisChannelHandler;
import io.lettuce.core.RedisChannelWriter;
import io.lettuce.core.RedisConnectionException;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.SslConnectionBuilder;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.internal.Futures;
import io.lettuce.core.internal.LettuceAssert;
import io.lettuce.core.masterreplica.MasterReplica;
import io.lettuce.core.protocol.CommandExpiryWriter;
import io.lettuce.core.protocol.CommandHandler;
import io.lettuce.core.protocol.DefaultEndpoint;
import io.lettuce.core.protocol.Endpoint;
import io.lettuce.core.protocol.PushHandler;
import io.lettuce.core.pubsub.PubSubEndpoint;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnectionImpl;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.sentinel.StatefulRedisSentinelConnectionImpl;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import reactor.core.publisher.Mono;

/**
 * A scalable and thread-safe <a href="http://redisearch.io/">RediSearch</a> client
 * supporting synchronous, asynchronous and reactive execution models. Multiple
 * threads may share one connection if they avoid blocking and transactional
 * operations such as BLPOP and MULTI/EXEC.
 * <p>
 * {@link RediSearchClient} can be used with:
 * <ul>
 * <li>Redis Standalone</li>
 * </ul>
 *
 * <p>
 * {@link RediSearchClient} is an expensive resource. It holds a set of netty's
 * {@link io.netty.channel.EventLoopGroup}'s that use multiple threads. Reuse
 * this instance as much as possible or share a {@link ClientResources} instance
 * amongst multiple client instances.
 *
 * @author Will Glozer
 * @author Mark Paluch
 * @author Julien Ruaux
 * @see RedisURI
 * @see StatefulRediSearchConnection
 * @see RedisFuture
 * @see reactor.core.publisher.Mono
 * @see reactor.core.publisher.Flux
 * @see RedisCodec
 * @see ClientOptions
 * @see ClientResources
 * @see MasterReplica
 */
public class RediSearchClient extends AbstractRedisClient {

	private static final InternalLogger logger = InternalLoggerFactory.getInstance(RediSearchClient.class);

	private static final RedisURI EMPTY_URI = new RedisURI();

	private final RedisURI redisURI;

	protected RediSearchClient(ClientResources clientResources, RedisURI redisURI) {

		super(clientResources);

		assertNotNull(redisURI);

		this.redisURI = redisURI;
		setDefaultTimeout(redisURI.getTimeout());
	}

	/**
	 * Creates a uri-less RediSearchClient. You can connect to different Redis
	 * servers but you must supply a {@link RedisURI} on connecting. Methods without
	 * having a {@link RedisURI} will fail with a
	 * {@link java.lang.IllegalStateException}. Non-private constructor to make
	 * {@link RediSearchClient} proxyable.
	 */
	protected RediSearchClient() {
		this(null, EMPTY_URI);
	}

	/**
	 * Creates a uri-less RediSearchClient with default {@link ClientResources}. You
	 * can connect to different Redis servers but you must supply a {@link RedisURI}
	 * on connecting. Methods without having a {@link RedisURI} will fail with a
	 * {@link java.lang.IllegalStateException}.
	 *
	 * @return a new instance of {@link RediSearchClient}
	 */
	public static RediSearchClient create() {
		return new RediSearchClient(null, EMPTY_URI);
	}

	/**
	 * Create a new client that connects to the supplied {@link RedisURI uri} with
	 * default {@link ClientResources}. You can connect to different Redis servers
	 * but you must supply a {@link RedisURI} on connecting.
	 *
	 * @param redisURI the Redis URI, must not be {@code null}
	 * @return a new instance of {@link RediSearchClient}
	 */
	public static RediSearchClient create(RedisURI redisURI) {
		assertNotNull(redisURI);
		return new RediSearchClient(null, redisURI);
	}

	/**
	 * Create a new client that connects to the supplied uri with default
	 * {@link ClientResources}. You can connect to different Redis servers but you
	 * must supply a {@link RedisURI} on connecting.
	 *
	 * @param uri the Redis URI, must not be {@code null}
	 * @return a new instance of {@link RediSearchClient}
	 */
	public static RediSearchClient create(String uri) {
		LettuceAssert.notEmpty(uri, "URI must not be empty");
		return new RediSearchClient(null, RedisURI.create(uri));
	}

	/**
	 * Creates a uri-less RediSearchClient with shared {@link ClientResources}. You
	 * need to shut down the {@link ClientResources} upon shutting down your
	 * application. You can connect to different Redis servers but you must supply a
	 * {@link RedisURI} on connecting. Methods without having a {@link RedisURI}
	 * will fail with a {@link java.lang.IllegalStateException}.
	 *
	 * @param clientResources the client resources, must not be {@code null}
	 * @return a new instance of {@link RediSearchClient}
	 */
	public static RediSearchClient create(ClientResources clientResources) {
		assertNotNull(clientResources);
		return new RediSearchClient(clientResources, EMPTY_URI);
	}

	/**
	 * Create a new client that connects to the supplied uri with shared
	 * {@link ClientResources}.You need to shut down the {@link ClientResources}
	 * upon shutting down your application. You can connect to different Redis
	 * servers but you must supply a {@link RedisURI} on connecting.
	 *
	 * @param clientResources the client resources, must not be {@code null}
	 * @param uri             the Redis URI, must not be {@code null}
	 *
	 * @return a new instance of {@link RediSearchClient}
	 */
	public static RediSearchClient create(ClientResources clientResources, String uri) {
		assertNotNull(clientResources);
		LettuceAssert.notEmpty(uri, "URI must not be empty");
		return create(clientResources, RedisURI.create(uri));
	}

	/**
	 * Create a new client that connects to the supplied {@link RedisURI uri} with
	 * shared {@link ClientResources}. You need to shut down the
	 * {@link ClientResources} upon shutting down your application.You can connect
	 * to different Redis servers but you must supply a {@link RedisURI} on
	 * connecting.
	 *
	 * @param clientResources the client resources, must not be {@code null}
	 * @param redisURI        the Redis URI, must not be {@code null}
	 * @return a new instance of {@link RediSearchClient}
	 */
	public static RediSearchClient create(ClientResources clientResources, RedisURI redisURI) {
		assertNotNull(clientResources);
		assertNotNull(redisURI);
		return new RediSearchClient(clientResources, redisURI);
	}

	/**
	 * Open a new connection to a Redis server that treats keys and values as UTF-8
	 * strings.
	 *
	 * @return A new stateful Redis connection
	 */
	public StatefulRediSearchConnection<String, String> connect() {
		return connect(newStringStringCodec());
	}

	/**
	 * Open a new connection to a Redis server. Use the supplied {@link RedisCodec
	 * codec} to encode/decode keys and values.
	 *
	 * @param codec Use this codec to encode/decode keys and values, must not be
	 *              {@code null}
	 * @param <K>   Key type
	 * @param <V>   Value type
	 * @return A new stateful Redis connection
	 */
	public <K, V> StatefulRediSearchConnection<K, V> connect(RedisCodec<K, V> codec) {

		checkForRedisURI();

		return getConnection(connectStandaloneAsync(codec, this.redisURI, getDefaultTimeout()));
	}

	/**
	 * Open a new connection to a Redis server using the supplied {@link RedisURI}
	 * that treats keys and values as UTF-8 strings.
	 *
	 * @param redisURI the Redis server to connect to, must not be {@code null}
	 * @return A new connection
	 */
	public StatefulRediSearchConnection<String, String> connect(RedisURI redisURI) {

		assertNotNull(redisURI);

		return getConnection(connectStandaloneAsync(newStringStringCodec(), redisURI, redisURI.getTimeout()));
	}

	/**
	 * Open a new connection to a Redis server using the supplied {@link RedisURI}
	 * and the supplied {@link RedisCodec codec} to encode/decode keys.
	 *
	 * @param codec    Use this codec to encode/decode keys and values, must not be
	 *                 {@code null}
	 * @param redisURI the Redis server to connect to, must not be {@code null}
	 * @param <K>      Key type
	 * @param <V>      Value type
	 * @return A new connection
	 */
	public <K, V> StatefulRediSearchConnection<K, V> connect(RedisCodec<K, V> codec, RedisURI redisURI) {

		assertNotNull(redisURI);

		return getConnection(connectStandaloneAsync(codec, redisURI, redisURI.getTimeout()));
	}

	/**
	 * Open asynchronously a new connection to a Redis server using the supplied
	 * {@link RedisURI} and the supplied {@link RedisCodec codec} to encode/decode
	 * keys.
	 *
	 * @param codec    Use this codec to encode/decode keys and values, must not be
	 *                 {@code null}
	 * @param redisURI the Redis server to connect to, must not be {@code null}
	 * @param <K>      Key type
	 * @param <V>      Value type
	 * @return {@link ConnectionFuture} to indicate success or failure to connect.
	 * @since 5.0
	 */
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

		DefaultEndpoint endpoint = new DefaultEndpoint(getOptions(), getResources());
		RedisChannelWriter writer = endpoint;

		if (CommandExpiryWriter.isSupported(getOptions())) {
			writer = new CommandExpiryWriter(writer, getOptions(), getResources());
		}

		StatefulRediSearchConnectionImpl<K, V> connection = newStatefulRediSearchConnection(writer, endpoint, codec,
				timeout);
		ConnectionFuture<StatefulRediSearchConnection<K, V>> future = connectStatefulAsync(connection, endpoint,
				redisURI, () -> new CommandHandler(getOptions(), getResources(), endpoint));

		future.whenComplete((channelHandler, throwable) -> {

			if (throwable != null) {
				connection.close();
			}
		});

		return future;
	}

	@SuppressWarnings("unchecked")
	private <K, V, S> ConnectionFuture<S> connectStatefulAsync(StatefulRediSearchConnectionImpl<K, V> connection,
			Endpoint endpoint, RedisURI redisURI, Supplier<CommandHandler> commandHandlerSupplier) {

		ConnectionBuilder connectionBuilder;
		if (redisURI.isSsl()) {
			SslConnectionBuilder sslConnectionBuilder = SslConnectionBuilder.sslConnectionBuilder();
			sslConnectionBuilder.ssl(redisURI);
			connectionBuilder = sslConnectionBuilder;
		} else {
			connectionBuilder = ConnectionBuilder.connectionBuilder();
		}

		ConnectionState state = connection.getConnectionState();
		state.apply(redisURI);
		state.setDb(redisURI.getDatabase());

		connectionBuilder.connection(connection);
		connectionBuilder.clientOptions(getOptions());
		connectionBuilder.clientResources(getResources());
		connectionBuilder.commandHandler(commandHandlerSupplier).endpoint(endpoint);

		connectionBuilder(getSocketAddressSupplier(redisURI), connectionBuilder, redisURI);
		connectionBuilder.connectionInitializer(createHandshake(state));
		channelType(connectionBuilder, redisURI);

		ConnectionFuture<RedisChannelHandler<K, V>> future = initializeChannelAsync(connectionBuilder);

		return future.thenApply(channelHandler -> (S) connection);
	}

	/**
	 * Set the {@link ClientOptions} for the client.
	 *
	 * @param clientOptions the new client options
	 * @throws IllegalArgumentException if {@literal clientOptions} is null
	 */
	@Override
	public void setOptions(ClientOptions clientOptions) {
		super.setOptions(clientOptions);
	}

	// -------------------------------------------------------------------------
	// Implementation hooks and helper methods
	// -------------------------------------------------------------------------

	/**
	 * Create a new instance of {@link StatefulRedisPubSubConnectionImpl} or a
	 * subclass.
	 * <p>
	 * Subclasses of {@link RediSearchClient} may override that method.
	 *
	 * @param endpoint      the endpoint
	 * @param channelWriter the channel writer
	 * @param codec         codec
	 * @param timeout       default timeout
	 * @param <K>           Key-Type
	 * @param <V>           Value Type
	 * @return new instance of StatefulRedisPubSubConnectionImpl
	 */
	protected <K, V> StatefulRedisPubSubConnectionImpl<K, V> newStatefulRedisPubSubConnection(
			PubSubEndpoint<K, V> endpoint, RedisChannelWriter channelWriter, RedisCodec<K, V> codec, Duration timeout) {
		return new StatefulRedisPubSubConnectionImpl<>(endpoint, channelWriter, codec, timeout);
	}

	/**
	 * Create a new instance of {@link StatefulRedisSentinelConnectionImpl} or a
	 * subclass.
	 * <p>
	 * Subclasses of {@link RediSearchClient} may override that method.
	 *
	 * @param channelWriter the channel writer
	 * @param codec         codec
	 * @param timeout       default timeout
	 * @param <K>           Key-Type
	 * @param <V>           Value Type
	 * @return new instance of StatefulRedisSentinelConnectionImpl
	 */
	protected <K, V> StatefulRedisSentinelConnectionImpl<K, V> newStatefulRedisSentinelConnection(
			RedisChannelWriter channelWriter, RedisCodec<K, V> codec, Duration timeout) {
		return new StatefulRedisSentinelConnectionImpl<>(channelWriter, codec, timeout);
	}

	/**
	 * Create a new instance of {@link StatefulRediSearchConnectionImpl} or a
	 * subclass.
	 * <p>
	 * Subclasses of {@link RediSearchClient} may override that method.
	 *
	 * @param channelWriter the channel writer
	 * @param pushHandler   the handler for push notifications
	 * @param codec         codec
	 * @param timeout       default timeout
	 * @param <K>           Key-Type
	 * @param <V>           Value Type
	 * @return new instance of StatefulRediSearchConnectionImpl
	 */
	protected <K, V> StatefulRediSearchConnectionImpl<K, V> newStatefulRediSearchConnection(
			RedisChannelWriter channelWriter, PushHandler pushHandler, RedisCodec<K, V> codec, Duration timeout) {
		return new StatefulRediSearchConnectionImpl<>(channelWriter, pushHandler, codec, timeout);
	}

	/**
	 * Get a {@link Mono} that resolves {@link RedisURI} to a {@link SocketAddress}.
	 * Resolution is performed either using Redis Sentinel (if the {@link RedisURI}
	 * is configured with Sentinels) or via DNS resolution.
	 * <p>
	 * Subclasses of {@link RediSearchClient} may override that method.
	 *
	 * @param redisURI must not be {@code null}.
	 * @return the resolved {@link SocketAddress}.
	 * @see ClientResources#dnsResolver()
	 * @see RedisURI#getSentinels()
	 * @see RedisURI#getSentinelMasterId()
	 */
	protected Mono<SocketAddress> getSocketAddress(RedisURI redisURI) {

		return Mono.defer(() -> {

			return Mono.fromCallable(() -> getResources().socketAddressResolver().resolve((redisURI)));
		});
	}

	/**
	 * Returns a {@link String} {@link RedisCodec codec}.
	 *
	 * @return a {@link String} {@link RedisCodec codec}.
	 * @see StringCodec#UTF8
	 */
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
				throw new IllegalArgumentException("RedisURI for Redis Sentinel requires a masterId");
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
				"RedisURI is not available. Use RediSearchClient(Host), RediSearchClient(Host, Port) or RediSearchClient(RedisURI) to construct your client.");
		checkValidRedisURI(this.redisURI);
	}

}
