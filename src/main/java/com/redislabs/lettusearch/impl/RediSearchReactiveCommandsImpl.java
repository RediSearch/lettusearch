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
import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.suggest.Suggestion;
import com.redislabs.lettusearch.suggest.SuggetOptions;

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
	public Mono<String> add(K index, Document<K, V> document, AddOptions options) {
		return createMono(() -> commandBuilder.add(index, document, options));
	}

	@Override
	public Mono<String> create(K index, Schema schema, CreateOptions options) {
		return createMono(() -> commandBuilder.create(index, schema, options));
	}

	@Override
	public Mono<String> drop(K index, DropOptions options) {
		return createMono(() -> commandBuilder.drop(index, options));
	}

	@Override
	public Flux<Object> ftInfo(K index) {
		return createDissolvingFlux(() -> commandBuilder.info(index));
	}

	@Override
	public Mono<Map<K, V>> get(K index, K docId) {
		return createMono(() -> commandBuilder.get(index, docId));
	}

	@SafeVarargs
	@Override
	public final Flux<Map<K, V>> ftMget(K index, K... docIds) {
		return createDissolvingFlux(() -> commandBuilder.mget(index, docIds));
	}

	@Override
	public Mono<Boolean> del(K index, K docId, boolean deleteDoc) {
		return createMono(() -> commandBuilder.del(index, docId, deleteDoc));
	}

	@Override
	public Mono<SearchResults<K, V>> search(K index, V query, SearchOptions options) {
		return createMono(() -> commandBuilder.search(index, query, options));
	}

	@Override
	public Mono<SearchResults<K, V>> search(K index, V query, Object... options) {
		return createMono(() -> commandBuilder.search(index, query, options));
	}

	@Override
	public Mono<AggregateResults<K, V>> aggregate(K index, V query, AggregateOptions options) {
		return createMono(() -> commandBuilder.aggregate(index, query, options));
	}

	@Override
	public Mono<AggregateResults<K, V>> aggregate(K index, V query, Object... options) {
		return createMono(() -> commandBuilder.aggregate(index, query, options));
	}

	@Override
	public Mono<AggregateWithCursorResults<K, V>> aggregate(K index, V query, Cursor cursor,
			AggregateOptions options) {
		return createMono(() -> commandBuilder.aggregate(index, query, cursor, options));
	}

	@Override
	public Mono<AggregateWithCursorResults<K, V>> aggregate(K index, V query, Cursor cursor,
			Object... options) {
		return createMono(() -> commandBuilder.aggregate(index, query, cursor, options));
	}

	@Override
	public Mono<AggregateWithCursorResults<K, V>> cursorRead(K index, long cursor, Long count) {
		return createMono(() -> commandBuilder.cursorRead(index, cursor, count));
	}

	@Override
	public Mono<String> cursorDelete(K index, long cursor) {
		return createMono(() -> commandBuilder.cursorDelete(index, cursor));
	}

	@Override
	public Mono<Long> sugadd(K key, Suggestion<V> suggestion, boolean increment) {
		return createMono(() -> commandBuilder.sugadd(key, suggestion, increment));
	}

	@Override
	public Flux<Suggestion<V>> sugget(K key, V prefix, SuggetOptions options) {
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
	public Mono<String> alter(K index, K field, FieldOptions options) {
		return createMono(() -> commandBuilder.alter(index, field, options));
	}

	@Override
	public Mono<String> aliasAdd(K name, K index) {
		return createMono(() -> commandBuilder.aliasAdd(name, index));
	}

	@Override
	public Mono<String> aliasUpdate(K name, K index) {
		return createMono(() -> commandBuilder.aliasUpdate(name, index));
	}

	@Override
	public Mono<String> aliasDel(K name) {
		return createMono(() -> commandBuilder.aliasDel(name));
	}

}
