package com.redislabs.lettusearch;

import java.time.Duration;

import com.redislabs.lettusearch.impl.StatefulRediSearchConnectionImpl;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisChannelWriter;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.internal.LettuceAssert;
import io.lettuce.core.masterreplica.MasterReplica;
import io.lettuce.core.protocol.PushHandler;
import io.lettuce.core.resource.ClientResources;

/**
 * A scalable and thread-safe <a href="http://redisearch.io/">RediSearch</a>
 * client supporting synchronous, asynchronous and reactive execution models.
 * Multiple threads may share one connection if they avoid blocking and
 * transactional operations such as BLPOP and MULTI/EXEC.
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
public class RediSearchClient extends RedisClient {

	protected RediSearchClient(ClientResources clientResources, RedisURI redisURI) {
		super(clientResources, redisURI);
	}

	/**
	 * Creates a uri-less RediSearchClient. You can connect to different Redis
	 * servers but you must supply a {@link RedisURI} on connecting. Methods without
	 * having a {@link RedisURI} will fail with a
	 * {@link java.lang.IllegalStateException}. Non-private constructor to make
	 * {@link RediSearchClient} proxyable.
	 */
	protected RediSearchClient() {
		super();
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
		return new RediSearchClient();
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
		return new RediSearchClient(clientResources, new RedisURI());
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

	private static void assertNotNull(ClientResources clientResources) {
		LettuceAssert.notNull(clientResources, "ClientResources must not be null");
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
	 * Open a new connection to a RediSearch server that treats keys and values as
	 * UTF-8 strings.
	 *
	 * @return A new stateful Redis connection
	 */
	@Override
	public StatefulRediSearchConnection<String, String> connect() {
		return connect(newStringStringCodec());
	}

	/**
	 * Open a new connection to a RediSearch server. Use the supplied
	 * {@link RedisCodec codec} to encode/decode keys and values.
	 *
	 * @param codec Use this codec to encode/decode keys and values, must not be
	 *              {@code null}
	 * @param <K>   Key type
	 * @param <V>   Value type
	 * @return A new stateful Redis connection
	 */
	@Override
	public <K, V> StatefulRediSearchConnection<K, V> connect(RedisCodec<K, V> codec) {
		return (StatefulRediSearchConnection<K, V>) super.connect(codec);
	}

	/**
	 * Open a new connection to a RediSearch server using the supplied
	 * {@link RedisURI} that treats keys and values as UTF-8 strings.
	 *
	 * @param redisURI the Redis server to connect to, must not be {@code null}
	 * @return A new connection
	 */
	@Override
	public StatefulRediSearchConnection<String, String> connect(RedisURI redisURI) {
		return (StatefulRediSearchConnection<String, String>) super.connect(redisURI);
	}

	/**
	 * Open a new connection to a RediSearch server using the supplied
	 * {@link RedisURI} and the supplied {@link RedisCodec codec} to encode/decode
	 * keys.
	 *
	 * @param codec    Use this codec to encode/decode keys and values, must not be
	 *                 {@code null}
	 * @param redisURI the Redis server to connect to, must not be {@code null}
	 * @param <K>      Key type
	 * @param <V>      Value type
	 * @return A new connection
	 */
	@Override
	public <K, V> StatefulRediSearchConnection<K, V> connect(RedisCodec<K, V> codec, RedisURI redisURI) {
		return (StatefulRediSearchConnection<K, V>) super.connect(codec, redisURI);
	}

	private static void assertNotNull(RedisURI redisURI) {
		LettuceAssert.notNull(redisURI, "RedisURI must not be null");
	}

	/**
	 * Create a new instance of {@link StatefulRediSearchConnectionImpl} or a
	 * subclass.
	 * <p>
	 * Subclasses of {@link RediSearchClient} may override that method.
	 *
	 * @param channelWriter the channel writer
	 * @param codec         codec
	 * @param timeout       default timeout
	 * @param <K>           Key-Type
	 * @param <V>           Value Type
	 * @return new instance of StatefulRediSearchConnectionImpl
	 */
	@Override
	protected <K, V> StatefulRediSearchConnectionImpl<K, V> newStatefulRedisConnection(RedisChannelWriter channelWriter,
			PushHandler pushHandler, RedisCodec<K, V> codec, Duration timeout) {
		return new StatefulRediSearchConnectionImpl<>(channelWriter, pushHandler, codec, timeout);
	}

}
