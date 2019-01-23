package com.redislabs.lettusearch.suggest.api.async;

import java.util.List;

import com.redislabs.lettusearch.suggest.SuggestAddOptions;
import com.redislabs.lettusearch.suggest.SuggestGetOptions;
import com.redislabs.lettusearch.suggest.SuggestResult;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.async.RedisAsyncCommands;

public interface SuggestAsyncCommands<K, V> extends RedisAsyncCommands<K, V> {

	RedisFuture<Long> sugadd(K key, V string, double score, SuggestAddOptions options);

	RedisFuture<List<SuggestResult<V>>> sugget(K key, V prefix, SuggestGetOptions options);

}