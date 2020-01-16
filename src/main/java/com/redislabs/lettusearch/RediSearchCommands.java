package com.redislabs.lettusearch;

import com.redislabs.lettusearch.aggregate.api.AggregateCommands;
import com.redislabs.lettusearch.index.api.IndexCommands;
import com.redislabs.lettusearch.search.api.SearchCommands;
import com.redislabs.lettusearch.suggest.api.SuggestCommands;

import io.lettuce.core.api.sync.RedisCommands;

public interface RediSearchCommands<K, V> extends RedisCommands<K, V>, IndexCommands<K, V>, SearchCommands<K, V>,
		AggregateCommands<K, V>, SuggestCommands<K, V> {

	StatefulRediSearchConnection<K, V> getStatefulConnection();
}
