package com.redislabs.lettusearch;

import com.redislabs.lettusearch.api.async.SearchAsyncCommands;
import com.redislabs.lettusearch.api.reactive.SearchReactiveCommands;
import com.redislabs.lettusearch.api.sync.SearchCommands;

import io.lettuce.core.api.StatefulRedisConnection;

public interface StatefulSearchConnection<K, V> extends StatefulRedisConnection<K, V> {

	SearchCommands<K, V> sync();

	SearchAsyncCommands<K, V> async();

	SearchReactiveCommands<K, V> reactive();
}