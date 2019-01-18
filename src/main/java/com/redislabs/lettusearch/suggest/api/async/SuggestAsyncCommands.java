package com.redislabs.lettusearch.suggest.api.async;

import java.util.List;

import com.redislabs.lettusearch.StatefulRediSearchConnection;
import com.redislabs.lettusearch.suggest.GetOptions;
import com.redislabs.lettusearch.suggest.SuggestResult;
import com.redislabs.lettusearch.suggest.Suggestion;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.async.RedisAsyncCommands;

public interface SuggestAsyncCommands<K, V> extends RedisAsyncCommands<K, V> {

	StatefulRediSearchConnection<K, V> getStatefulConnection();

	RedisFuture<Long> add(String key, Suggestion suggestion);

	RedisFuture<List<SuggestResult<V>>> get(K key, V prefix, GetOptions options);

}