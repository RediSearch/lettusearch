package com.redislabs.lettusearch;

import java.util.Map;

import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.AggregateResults;
import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.DropOptions;
import com.redislabs.lettusearch.search.Schema;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.suggest.SuggestAddOptions;
import com.redislabs.lettusearch.suggest.SuggestGetOptions;
import com.redislabs.lettusearch.suggest.SuggestResult;

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
	public Mono<String> add(String index, K docId, double score, Map<K, V> fields, AddOptions options) {
		return createMono(() -> commandBuilder.add(index, docId, score, fields, options));
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
	public Mono<AggregateResults<K, V>> aggregate(String index, String query, AggregateOptions options) {
		return createMono(() -> commandBuilder.aggregate(index, query, options));
	}

	@Override
	public Mono<Long> sugadd(K key, V string, double score, SuggestAddOptions options) {
		return createMono(() -> commandBuilder.sugadd(key, string, score, options));
	}

	@Override
	public Flux<SuggestResult<V>> sugget(K key, V prefix, SuggestGetOptions options) {
		return createDissolvingFlux(() -> commandBuilder.sugget(key, prefix, options));
	}

	@Override
	public StatefulRediSearchConnection<K, V> getStatefulConnection() {
		return (StatefulRediSearchConnection<K, V>) super.getStatefulConnection();
	}

}
