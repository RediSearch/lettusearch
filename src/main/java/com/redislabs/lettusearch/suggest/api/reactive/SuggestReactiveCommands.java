package com.redislabs.lettusearch.suggest.api.reactive;

import com.redislabs.lettusearch.suggest.GetOptions;
import com.redislabs.lettusearch.suggest.SuggestResult;
import com.redislabs.lettusearch.suggest.Suggestion;

import io.lettuce.core.api.reactive.RedisReactiveCommands;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SuggestReactiveCommands<K, V> extends RedisReactiveCommands<K, V> {

	Mono<Long> add(String key, Suggestion suggestion);

	Flux<SuggestResult<V>> get(K key, V prefix, GetOptions options);

}