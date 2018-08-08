package com.redislabs.lettusearch;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.resource.ClientResources;

public class RediSearchClient {

	private RedisClient redisClient;

	protected RediSearchClient(RedisClient redisClient) {
		this.redisClient = redisClient;
	}

	public RediSearchConnection<String, String> connect() {
		return new RediSearchConnection<String, String>(redisClient.connect());
	}

	public <K, V> RediSearchConnection<K, V> connect(RedisCodec<K, V> codec) {
		return new RediSearchConnection<K, V>(redisClient.connect(codec));
	}

	public RediSearchConnection<String, String> connect(RedisURI redisURI) {
		return new RediSearchConnection<String, String>(redisClient.connect(redisURI));
	}

	public <K, V> RediSearchConnection<K, V> connect(RedisCodec<K, V> codec, RedisURI redisURI) {
		return new RediSearchConnection<K, V>(redisClient.connect(codec, redisURI));
	}

	public static RediSearchClient create(RedisClient client) {
		return new RediSearchClient(client);
	}

	public static RediSearchClient create() {
		return new RediSearchClient(RedisClient.create());
	}

	public static RediSearchClient create(RedisURI redisURI) {
		return new RediSearchClient(RedisClient.create(redisURI));
	}

	public static RediSearchClient create(String uri) {
		return new RediSearchClient(RedisClient.create(uri));
	}

	public static RediSearchClient create(ClientResources clientResources) {
		return new RediSearchClient(RedisClient.create(clientResources));
	}

	public static RediSearchClient create(ClientResources clientResources, String uri) {
		return new RediSearchClient(RedisClient.create(clientResources, uri));
	}

	public static RediSearchClient create(ClientResources clientResources, RedisURI redisURI) {
		return new RediSearchClient(RedisClient.create(clientResources, redisURI));
	}

	public void setOptions(ClientOptions clientOptions) {
		redisClient.setOptions(clientOptions);
	}

	public void shutdown() {
		redisClient.shutdown();
	}

	public void shutdown(Duration quietPeriod, Duration timeout) {
		redisClient.shutdown(quietPeriod, timeout);
	}

	public void shutdown(long quietPeriod, long timeout, TimeUnit timeUnit) {
		redisClient.shutdown(quietPeriod, timeout, timeUnit);
	}

	public CompletableFuture<Void> shutdownAsync() {
		return redisClient.shutdownAsync();
	}

	public CompletableFuture<Void> shutdownAsync(long quietPeriod, long timeout, TimeUnit timeUnit) {
		return redisClient.shutdownAsync(quietPeriod, timeout, timeUnit);
	}

}
