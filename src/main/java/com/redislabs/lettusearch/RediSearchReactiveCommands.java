package com.redislabs.lettusearch;

import com.redislabs.lettusearch.search.api.reactive.SearchReactiveCommands;
import com.redislabs.lettusearch.suggest.api.reactive.SuggestReactiveCommands;

import io.lettuce.core.api.reactive.RedisReactiveCommands;

public interface RediSearchReactiveCommands<K, V>
		extends RedisReactiveCommands<K, V>, SearchReactiveCommands<K, V>, SuggestReactiveCommands<K, V> {

	StatefulRediSearchConnection<K, V> getStatefulConnection();
}
