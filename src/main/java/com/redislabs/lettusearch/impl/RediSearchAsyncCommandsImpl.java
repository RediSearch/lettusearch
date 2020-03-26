package com.redislabs.lettusearch.impl;

import java.util.List;
import java.util.Map;

import com.redislabs.lettusearch.RediSearchAsyncCommands;
import com.redislabs.lettusearch.RediSearchCommandBuilder;
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

import io.lettuce.core.RedisAsyncCommandsImpl;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.codec.RedisCodec;

public class RediSearchAsyncCommandsImpl<K, V> extends RedisAsyncCommandsImpl<K, V>
		implements RediSearchAsyncCommands<K, V> {

	private final StatefulRediSearchConnection<K, V> connection;
	private final RediSearchCommandBuilder<K, V> commandBuilder;

	public RediSearchAsyncCommandsImpl(StatefulRediSearchConnection<K, V> connection, RedisCodec<K, V> codec) {
		super(connection, codec);
		this.connection = connection;
		this.commandBuilder = new RediSearchCommandBuilder<>(codec);
	}

	@Override
	public StatefulRediSearchConnection<K, V> getStatefulConnection() {
		return connection;
	}

	@Override
	public RedisFuture<String> add(String index, AddArgs<K, V> args) {
		return add(index, args.getDocument().getId(), args.getDocument().getScore(), args.getDocument().getFields(),
				args.getPayload(), args.getOptions());
	}

	@Override
	public RedisFuture<String> add(String index, K docId, double score, Map<K, V> fields, V payload,
			AddOptions options) {
		return dispatch(commandBuilder.add(index, docId, score, fields, payload, options));
	}

	@Override
	public RedisFuture<String> create(String index, Schema schema) {
		return create(index, schema, null);
	}

	@Override
	public RedisFuture<String> create(String index, Schema schema, CreateOptions options) {
		return dispatch(commandBuilder.create(index, schema, options));
	}

	@Override
	public RedisFuture<String> drop(String index) {
		return drop(index, null);
	}

	@Override
	public RedisFuture<String> drop(String index, DropOptions options) {
		return dispatch(commandBuilder.drop(index, options));
	}

	@Override
	public RedisFuture<List<Object>> indexInfo(String index) {
		return dispatch(commandBuilder.info(index));
	}

	@Override
	public RedisFuture<SearchResults<K, V>> search(String index, String query) {
		return search(index, SearchArgs.builder().query(query).build());
	}

	@Override
	public RedisFuture<SearchResults<K, V>> search(String index, SearchArgs args) {
		return dispatch(commandBuilder.search(index, args.getQuery(), args.getOptions()));
	}

	@Override
	public RedisFuture<AggregateWithCursorResults<K, V>> aggregate(String index, AggregateArgs args, Cursor cursor) {
		return dispatch(commandBuilder.aggregate(index, args.getQuery(), args.getOptions(), cursor));
	}

	@Override
	public RedisFuture<AggregateResults<K, V>> aggregate(String index, AggregateArgs args) {
		return dispatch(commandBuilder.aggregate(index, args.getQuery(), args.getOptions()));
	}

	@Override
	public RedisFuture<AggregateWithCursorResults<K, V>> cursorRead(String index, CursorArgs args) {
		return dispatch(commandBuilder.cursorRead(index, args.getCursor(), args.getCount()));
	}

	@Override
	public RedisFuture<String> cursorDelete(String index, long cursor) {
		return dispatch(commandBuilder.cursorDelete(index, cursor));
	}

	@Override
	public RedisFuture<Long> sugadd(K key, SugaddArgs<V> args) {
		return sugadd(key, args.getString(), args.getScore(), args.isIncrement(), args.getPayload());
	}

	@Override
	public RedisFuture<Long> sugadd(K key, V string, double score, boolean increment, V payload) {
		return dispatch(commandBuilder.sugadd(key, string, score, increment, payload));
	}

	@Override
	public RedisFuture<List<SuggetResult<V>>> sugget(K key, SuggetArgs<V> args) {
		return sugget(key, args.getPrefix(), args.getOptions());
	}

	@Override
	public RedisFuture<List<SuggetResult<V>>> sugget(K key, V prefix, SuggetOptions options) {
		return dispatch(commandBuilder.sugget(key, prefix, options));
	}

	@Override
	public RedisFuture<Boolean> sugdel(K key, V string) {
		return dispatch(commandBuilder.sugdel(key, string));
	}

	@Override
	public RedisFuture<Long> suglen(K key) {
		return dispatch(commandBuilder.suglen(key));
	}

	@Override
	public RedisFuture<Map<K, V>> get(String index, K docId) {
		return dispatch(commandBuilder.get(index, docId));
	}

	@Override
	public RedisFuture<List<Map<K, V>>> ftMget(String index, @SuppressWarnings("unchecked") K... docIds) {
		return dispatch(commandBuilder.mget(index, docIds));
	}

	@Override
	public RedisFuture<Boolean> del(String index, DelArgs<K> args) {
		return del(index, args.getDocumentId(), args.isDeleteDocument());
	}

	@Override
	public RedisFuture<Boolean> del(String index, K docId, boolean deleteDoc) {
		return dispatch(commandBuilder.del(index, docId, deleteDoc));
	}

	@Override
	public RedisFuture<String> alter(String index, K field, FieldOptions options) {
		return dispatch(commandBuilder.alter(index, field, options));
	}

	@Override
	public RedisFuture<String> aliasAdd(String name, String index) {
		return dispatch(commandBuilder.aliasAdd(name, index));
	}

	@Override
	public RedisFuture<String> aliasDel(String name) {
		return dispatch(commandBuilder.aliasDel(name));
	}

	@Override
	public RedisFuture<String> aliasUpdate(String name, String index) {
		return dispatch(commandBuilder.aliasUpdate(name, index));
	}

}
