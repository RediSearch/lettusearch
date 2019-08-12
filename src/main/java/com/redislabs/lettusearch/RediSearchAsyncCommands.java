package com.redislabs.lettusearch;

import com.redislabs.lettusearch.search.api.async.SearchAsyncCommands;
import com.redislabs.lettusearch.suggest.api.async.SuggestAsyncCommands;

import io.lettuce.core.api.async.RedisAsyncCommands;

public interface RediSearchAsyncCommands<K, V> extends RedisAsyncCommands<K, V>, SearchAsyncCommands<K, V>, SuggestAsyncCommands<K, V> {

	StatefulRediSearchConnection<K, V> getStatefulConnection();
}
