package com.redislabs.lettusearch.impl;

import java.util.Map;

import com.redislabs.lettusearch.RediSearchCommandBuilder;
import com.redislabs.lettusearch.RediSearchReactiveCommands;
import com.redislabs.lettusearch.StatefulRediSearchConnection;
import com.redislabs.lettusearch.aggregate.AggregateArgs;
import com.redislabs.lettusearch.aggregate.AggregateResults;
import com.redislabs.lettusearch.aggregate.AggregateWithCursorResults;
import com.redislabs.lettusearch.aggregate.Cursor;
import com.redislabs.lettusearch.aggregate.CursorArgs;
import com.redislabs.lettusearch.search.AddArgs;
import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.CreateOptions;
import com.redislabs.lettusearch.search.DelArgs;
import com.redislabs.lettusearch.search.DropOptions;
import com.redislabs.lettusearch.search.Schema;
import com.redislabs.lettusearch.search.SearchArgs;
import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.search.field.FieldOptions;
import com.redislabs.lettusearch.suggest.SugaddArgs;
import com.redislabs.lettusearch.suggest.SuggetArgs;
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
	public Mono<String> add(String index, AddArgs<K, V> args) {
		return add(index, args.getDocument().getId(), args.getDocument().getScore(), args.getDocument().getFields(),
				args.getPayload(), args.getOptions());
	}

	@Override
	public Mono<String> add(String index, K docId, double score, Map<K, V> fields, V payload, AddOptions options) {
		return createMono(() -> commandBuilder.add(index, docId, score, fields, payload, options));
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
	public Mono<Boolean> del(String index, DelArgs<K> args) {
		return del(index, args.getDocumentId(), args.isDeleteDocument());
	}

	@Override
	public Mono<Boolean> del(String index, K docId, boolean deleteDoc) {
		return createMono(() -> commandBuilder.del(index, docId, deleteDoc));
	}
	
	@Override
	public Mono<SearchResults<K, V>> search(String index, String query) {
		return search(index, SearchArgs.builder().query(query).build());
	}

	@Override
	public Mono<SearchResults<K, V>> search(String index, SearchArgs args) {
		return createMono(() -> commandBuilder.search(index, args.getQuery(), args.getOptions()));
	}

	@Override
	public Mono<AggregateResults<K, V>> aggregate(String index, AggregateArgs args) {
		return createMono(() -> commandBuilder.aggregate(index, args.getQuery(), args.getOptions()));
	}

	@Override
	public Mono<AggregateWithCursorResults<K, V>> aggregate(String index, AggregateArgs args, Cursor cursor) {
		return createMono(() -> commandBuilder.aggregate(index, args.getQuery(), args.getOptions(), cursor));
	}

	@Override
	public Mono<AggregateWithCursorResults<K, V>> cursorRead(String index, CursorArgs args) {
		return createMono(() -> commandBuilder.cursorRead(index, args.getCursor(), args.getCount()));
	}

	@Override
	public Mono<String> cursorDelete(String index, long cursor) {
		return createMono(() -> commandBuilder.cursorDelete(index, cursor));
	}

	@Override
	public Mono<Long> sugadd(K key, SugaddArgs<V> args) {
		return sugadd(key, args.getString(), args.getScore(), args.isIncrement(), args.getPayload());
	}

	@Override
	public Mono<Long> sugadd(K key, V string, double score, boolean increment, V payload) {
		return createMono(() -> commandBuilder.sugadd(key, string, score, increment, payload));
	}

	@Override
	public Flux<SuggetResult<V>> sugget(K key, SuggetArgs<V> args) {
		return sugget(key, args.getPrefix(), args.getOptions());
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
