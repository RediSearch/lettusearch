package com.redislabs.lettusearch.impl;

import java.util.Map;

import com.redislabs.lettusearch.RediSearchCommandBuilder;
import com.redislabs.lettusearch.RediSearchReactiveCommands;
import com.redislabs.lettusearch.StatefulRediSearchConnection;
import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.AggregateResults;
import com.redislabs.lettusearch.aggregate.AggregateWithCursorResults;
import com.redislabs.lettusearch.aggregate.Cursor;
import com.redislabs.lettusearch.index.CreateOptions;
import com.redislabs.lettusearch.index.DropOptions;
import com.redislabs.lettusearch.index.Schema;
import com.redislabs.lettusearch.index.field.FieldOptions;
import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.suggest.SuggetOptions;
import com.redislabs.lettusearch.suggest.SuggetResult;

import io.lettuce.core.RedisReactiveCommandsImpl;
import io.lettuce.core.codec.RedisCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class RediSearchReactiveCommandsImpl<K, V> extends RedisReactiveCommandsImpl<K, V>
		implements RediSearchReactiveCommands<K, V> {

	private final StatefulRediSearchConnection<K, V> connection;
	private final RediSearchCommandBuilder<K, V> commandBuilder;

	public RediSearchReactiveCommandsImpl(StatefulRediSearchConnection<K, V> connection, RedisCodec<K, V> codec) {
		super(connection, codec);
		this.connection = connection;
		this.commandBuilder = new RediSearchCommandBuilder<>(codec);
	}

	@Override
	public StatefulRediSearchConnection<K, V> getStatefulConnection() {
		return connection;
	}

	@Override
	public Mono<String> add(String index, K docId, double score, Map<K, V> fields, V payload, AddOptions options) {
		return createMono(() -> commandBuilder.add(index, docId, score, fields, payload, options));
	}

	@Override
	public Mono<String> create(String index, Schema schema, CreateOptions options) {
		return createMono(() -> commandBuilder.create(index, schema, options));
	}

	@Override
	public Mono<String> drop(String index, DropOptions options) {
		return createMono(() -> commandBuilder.drop(index, options));
	}

	@Override
	public Flux<Object> ftInfo(String index) {
		return createDissolvingFlux(() -> commandBuilder.info(index));
	}

	@Override
	public Mono<Map<K, V>> get(String index, K docId) {
		return createMono(() -> commandBuilder.get(index, docId));
	}

	@SafeVarargs
	@Override
	public final Flux<Map<K, V>> ftMget(String index, K... docIds) {
		return createDissolvingFlux(() -> commandBuilder.mget(index, docIds));
	}

	@Override
	public Mono<Boolean> del(String index, K docId, boolean deleteDoc) {
		return createMono(() -> commandBuilder.del(index, docId, deleteDoc));
	}

	@Override
	public Mono<SearchResults<K, V>> search(String index, String query, SearchOptions options) {
		return createMono(() -> commandBuilder.search(index, query, options));
	}

	@Override
	public Mono<SearchResults<K, V>> search(String index, String query, Object... options) {
		return createMono(() -> commandBuilder.search(index, query, options));
	}

	@Override
	public Mono<AggregateResults<K, V>> aggregate(String index, String query, AggregateOptions options) {
		return createMono(() -> commandBuilder.aggregate(index, query, options));
	}

	@Override
	public Mono<AggregateResults<K, V>> aggregate(String index, String query, Object... options) {
		return createMono(() -> commandBuilder.aggregate(index, query, options));
	}

	@Override
	public Mono<AggregateWithCursorResults<K, V>> aggregate(String index, String query, Cursor cursor,
			AggregateOptions options) {
		return createMono(() -> commandBuilder.aggregate(index, query, cursor, options));
	}

	@Override
	public Mono<AggregateWithCursorResults<K, V>> aggregate(String index, String query, Cursor cursor,
			Object... options) {
		return createMono(() -> commandBuilder.aggregate(index, query, cursor, options));
	}

	@Override
	public Mono<AggregateWithCursorResults<K, V>> cursorRead(String index, long cursor, Long count) {
		return createMono(() -> commandBuilder.cursorRead(index, cursor, count));
	}

	@Override
	public Mono<String> cursorDelete(String index, long cursor) {
		return createMono(() -> commandBuilder.cursorDelete(index, cursor));
	}

	@Override
	public Mono<Long> sugadd(K key, V string, double score, boolean increment, V payload) {
		return createMono(() -> commandBuilder.sugadd(key, string, score, increment, payload));
	}

	@Override
	public Flux<SuggetResult<V>> sugget(K key, V prefix, SuggetOptions options) {
		return createDissolvingFlux(() -> commandBuilder.sugget(key, prefix, options));
	}

	@Override
	public Mono<Boolean> sugdel(K key, V string) {
		return createMono(() -> commandBuilder.sugdel(key, string));
	}

	@Override
	public Mono<Long> suglen(K key) {
		return createMono(() -> commandBuilder.suglen(key));
	}

	@Override
	public Mono<String> alter(String index, K field, FieldOptions options) {
		return createMono(() -> commandBuilder.alter(index, field, options));
	}

	@Override
	public Mono<String> aliasAdd(String name, String index) {
		return createMono(() -> commandBuilder.aliasAdd(name, index));
	}

	@Override
	public Mono<String> aliasUpdate(String name, String index) {
		return createMono(() -> commandBuilder.aliasUpdate(name, index));
	}

	@Override
	public Mono<String> aliasDel(String name) {
		return createMono(() -> commandBuilder.aliasDel(name));
	}

}
