package com.redislabs.lettusearch;

import com.redislabs.lettusearch.aggregate.api.AggregateAsyncCommands;
import com.redislabs.lettusearch.index.api.IndexAsyncCommands;
import com.redislabs.lettusearch.search.api.SearchAsyncCommands;
import com.redislabs.lettusearch.suggest.api.SuggestAsyncCommands;

import io.lettuce.core.api.async.RedisAsyncCommands;

public interface RediSearchAsyncCommands<K, V> extends RedisAsyncCommands<K, V>, IndexAsyncCommands<K, V>,
		SearchAsyncCommands<K, V>, AggregateAsyncCommands<K, V>, SuggestAsyncCommands<K, V> {

	StatefulRediSearchConnection<K, V> getStatefulConnection();
}
