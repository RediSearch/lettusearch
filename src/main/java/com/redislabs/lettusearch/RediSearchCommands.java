package com.redislabs.lettusearch;

import com.redislabs.lettusearch.search.api.sync.SearchCommands;
import com.redislabs.lettusearch.suggest.api.sync.SuggestCommands;

import io.lettuce.core.api.sync.RedisCommands;

public interface RediSearchCommands<K, V> extends RedisCommands<K, V>, SearchCommands<K, V>, SuggestCommands<K, V> {

	StatefulRediSearchConnection<K, V> getStatefulConnection();
}
