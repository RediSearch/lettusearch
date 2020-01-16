package com.redislabs.lettusearch.impl;

import java.util.Map;

import com.redislabs.lettusearch.RediSearchCommandBuilder;
import com.redislabs.lettusearch.RediSearchReactiveCommands;
import com.redislabs.lettusearch.StatefulRediSearchConnection;
import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.AggregateResults;
import com.redislabs.lettusearch.aggregate.AggregateWithCursorResults;
import com.redislabs.lettusearch.aggregate.CursorOptions;
import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.CreateOptions;
import com.redislabs.lettusearch.search.DropOptions;
import com.redislabs.lettusearch.search.Schema;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.search.field.FieldOptions;
import com.redislabs.lettusearch.suggest.SuggestGetOptions;
import com.redislabs.lettusearch.suggest.SuggestResult;

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
	public Mono<String> add(String index, K docId, double score, Map<K, V> fields) {
		return add(index, docId, score, fields, null, null);
	}

	@Override
	public Mono<String> add(String index, K docId, double score, Map<K, V> fields, V payload) {
		return add(index, docId, score, fields, null, payload);
	}

	public Mono<String> add(String index, K docId, double score, Map<K, V> fields, AddOptions options) {
		return add(index, docId, score, fields, options, null);
	}

	@Override
	public Mono<String> add(String index, K docId, double score, Map<K, V> fields, AddOptions options, V payload) {
		return createMono(() -> commandBuilder.add(index, docId, score, fields, options, payload));
	}

	@Override
	public Mono<String> create(String index, Schema schema) {
		return create(index, schema, null);
	}

	@Override
	public Mono<String> create(String index, Schema schema, CreateOptions options) {
		return createMono(() -> commandBuilder.create(index, schema, options));
	}

	@Override
	public Mono<String> drop(String index) {
		return drop(index, null);
	}

	@Override
	public Mono<String> drop(String index, DropOptions options) {
		return createMono(() -> commandBuilder.drop(index, options));
	}

	@Override
	public Flux<Object> indexInfo(String index) {
		return createDissolvingFlux(() -> commandBuilder.info(index));
	}

	@Override
	public Mono<Map<K, V>> get(String index, K docId) {
		return createMono(() -> commandBuilder.get(index, docId));
	}

	@Override
	public Flux<Map<K, V>> ftMget(String index, @SuppressWarnings("unchecked") K... docIds) {
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
	public Mono<SearchResults<K, V>> search(String index, String query) {
		return search(index, query, null);
	}

	@Override
	public Mono<AggregateResults<K, V>> aggregate(String index, String query, AggregateOptions options) {
		return createMono(() -> commandBuilder.aggregate(index, query, options));
	}

	@Override
	public Mono<AggregateWithCursorResults<K, V>> aggregate(String index, String query, AggregateOptions options,
			CursorOptions cursorOptions) {
		return createMono(() -> commandBuilder.aggregate(index, query, options, cursorOptions));
	}

	@Override
	public Mono<AggregateWithCursorResults<K, V>> cursorRead(String index, long cursor) {
		return createMono(() -> commandBuilder.cursorRead(index, cursor, null));
	}

	@Override
	public Mono<AggregateWithCursorResults<K, V>> cursorRead(String index, long cursor, long count) {
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
	public Mono<Long> sugadd(K key, V string, double score) {
		return sugadd(key, string, score, false, null);
	}

	@Override
	public Mono<Long> sugadd(K key, V string, double score, boolean increment) {
		return sugadd(key, string, score, increment, null);
	}

	@Override
	public Mono<Long> sugadd(K key, V string, double score, V payload) {
		return sugadd(key, string, score, false, payload);
	}

	@Override
	public Flux<SuggestResult<V>> sugget(K key, V prefix, SuggestGetOptions options) {
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
