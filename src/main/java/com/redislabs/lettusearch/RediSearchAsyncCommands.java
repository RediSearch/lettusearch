package com.redislabs.lettusearch;

import com.redislabs.lettusearch.search.api.async.SearchAsyncCommands;
import com.redislabs.lettusearch.suggest.api.async.SuggestAsyncCommands;

public interface RediSearchAsyncCommands<K, V> extends SearchAsyncCommands<K, V>, SuggestAsyncCommands<K, V> {

	StatefulRediSearchConnection<K, V> getStatefulConnection();
}
