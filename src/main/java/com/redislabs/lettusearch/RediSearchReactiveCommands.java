package com.redislabs.lettusearch;

import com.redislabs.lettusearch.aggregate.api.AggregateReactiveCommands;
import com.redislabs.lettusearch.index.api.IndexReactiveCommands;
import com.redislabs.lettusearch.search.api.SearchReactiveCommands;
import com.redislabs.lettusearch.suggest.api.SuggestReactiveCommands;

import io.lettuce.core.api.reactive.RedisReactiveCommands;

public interface RediSearchReactiveCommands<K, V> extends RedisReactiveCommands<K, V>, IndexReactiveCommands<K, V>,
		SearchReactiveCommands<K, V>, AggregateReactiveCommands<K, V>, SuggestReactiveCommands<K, V> {

	StatefulRediSearchConnection<K, V> getStatefulConnection();
}
