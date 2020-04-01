package com.redislabs.lettusearch.impl;

import java.util.List;
import java.util.Map;

import com.redislabs.lettusearch.RediSearchAsyncCommands;
import com.redislabs.lettusearch.RediSearchCommandBuilder;
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
	public RedisFuture<String> add(String index, Document<K, V> document, AddOptions options) {
		return dispatch(commandBuilder.add(index, document, options));
	}

	@Override
	public RedisFuture<String> create(String index, Schema schema, CreateOptions options) {
		return dispatch(commandBuilder.create(index, schema, options));
	}

	@Override
	public RedisFuture<String> drop(String index, DropOptions options) {
		return dispatch(commandBuilder.drop(index, options));
	}

	@Override
	public RedisFuture<List<Object>> ftInfo(String index) {
		return dispatch(commandBuilder.info(index));
	}

	@Override
	public RedisFuture<SearchResults<K, V>> search(String index, String query, SearchOptions options) {
		return dispatch(commandBuilder.search(index, query, options));
	}

	@Override
	public RedisFuture<SearchResults<K, V>> search(String index, String query, Object... options) {
		return dispatch(commandBuilder.search(index, query, options));
	}

	@Override
	public RedisFuture<AggregateWithCursorResults<K, V>> aggregate(String index, String query, Cursor cursor,
			AggregateOptions options) {
		return dispatch(commandBuilder.aggregate(index, query, cursor, options));
	}

	@Override
	public RedisFuture<AggregateWithCursorResults<K, V>> aggregate(String index, String query, Cursor cursor,
			Object... options) {
		return dispatch(commandBuilder.aggregate(index, query, cursor, options));
	}

	@Override
	public RedisFuture<AggregateResults<K, V>> aggregate(String index, String query, AggregateOptions options) {
		return dispatch(commandBuilder.aggregate(index, query, options));
	}

	@Override
	public RedisFuture<AggregateResults<K, V>> aggregate(String index, String query, Object... options) {
		return dispatch(commandBuilder.aggregate(index, query, options));
	}

	@Override
	public RedisFuture<AggregateWithCursorResults<K, V>> cursorRead(String index, long cursor, Long count) {
		return dispatch(commandBuilder.cursorRead(index, cursor, count));
	}

	@Override
	public RedisFuture<String> cursorDelete(String index, long cursor) {
		return dispatch(commandBuilder.cursorDelete(index, cursor));
	}

	@Override
	public RedisFuture<Long> sugadd(K key, Suggestion<V> suggestion, boolean increment) {
		return dispatch(commandBuilder.sugadd(key, suggestion, increment));
	}

	@Override
	public RedisFuture<List<Suggestion<V>>> sugget(K key, V prefix, SuggetOptions options) {
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
