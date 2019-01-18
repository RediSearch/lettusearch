package com.redislabs.lettusearch;

import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.search.DropOptions;
import com.redislabs.lettusearch.search.Schema;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.suggest.GetOptions;
import com.redislabs.lettusearch.suggest.SuggestResult;
import com.redislabs.lettusearch.suggest.Suggestion;

import io.lettuce.core.RedisReactiveCommandsImpl;
import io.lettuce.core.codec.RedisCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class RediSearchReactiveCommandsImpl<K, V> extends RedisReactiveCommandsImpl<K, V>
		implements RediSearchReactiveCommands<K, V> {

	private final RediSearchCommandBuilder<K, V> commandBuilder;

	public RediSearchReactiveCommandsImpl(StatefulRediSearchConnection<K, V> connection, RedisCodec<K, V> codec) {
		super(connection, codec);
		this.commandBuilder = new RediSearchCommandBuilder<>(codec);
	}

	@Override
	public Mono<String> add(String index, Document document) {
		return createMono(() -> commandBuilder.add(index, document));
	}

	@Override
	public Mono<String> create(String index, Schema schema) {
		return createMono(() -> commandBuilder.create(index, schema));
	}

	@Override
	public Mono<String> drop(String index, DropOptions options) {
		return createMono(() -> commandBuilder.drop(index, options));
	}

	@Override
	public Mono<SearchResults<K, V>> search(String index, String query, SearchOptions options) {
		return createMono(() -> commandBuilder.search(index, query, options));
	}

	@Override
	public Mono<Long> add(String key, Suggestion suggestion) {
		return createMono(() -> commandBuilder.add(key, suggestion));
	}

	@Override
	public Flux<SuggestResult<V>> get(K key, V prefix, GetOptions options) {
		return createDissolvingFlux(() -> commandBuilder.get(key, prefix, options));
	}

	@Override
	public StatefulRediSearchConnection<K, V> getStatefulConnection() {
		return (StatefulRediSearchConnection<K, V>) super.getStatefulConnection();
	}

}
