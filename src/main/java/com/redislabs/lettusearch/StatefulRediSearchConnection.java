package com.redislabs.lettusearch;

import io.lettuce.core.api.StatefulRedisConnection;

public interface StatefulRediSearchConnection<K, V> extends StatefulRedisConnection<K, V> {

	RediSearchCommands<K, V> sync();

	RediSearchAsyncCommands<K, V> async();

	RediSearchReactiveCommands<K, V> reactive();
}