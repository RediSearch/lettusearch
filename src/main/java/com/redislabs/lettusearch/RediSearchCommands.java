package com.redislabs.lettusearch;

import com.redislabs.lettusearch.aggregate.api.sync.AggregateCommands;
import com.redislabs.lettusearch.search.api.sync.SearchCommands;
import com.redislabs.lettusearch.suggest.api.sync.SuggestCommands;

public interface RediSearchCommands<K, V> extends SearchCommands<K, V>, AggregateCommands<K, V>, SuggestCommands<K, V> {

	StatefulRediSearchConnection<K, V> getStatefulConnection();
}
