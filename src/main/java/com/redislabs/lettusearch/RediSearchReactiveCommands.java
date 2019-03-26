package com.redislabs.lettusearch;

import com.redislabs.lettusearch.search.api.reactive.SearchReactiveCommands;
import com.redislabs.lettusearch.suggest.api.reactive.SuggestReactiveCommands;

public interface RediSearchReactiveCommands<K, V> extends SearchReactiveCommands<K, V>, SuggestReactiveCommands<K, V> {

	StatefulRediSearchConnection<K, V> getStatefulConnection();
}
