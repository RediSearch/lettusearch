package com.redislabs.lettusearch;

import java.util.List;
import java.util.Map;

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
	public RedisFuture<String> add(String index, K docId, double score, Map<K, V> fields) {
		return add(index, docId, score, fields, (AddOptions) null);
	}

	@Override
	public RedisFuture<String> add(String index, K docId, double score, Map<K, V> fields, V payload) {
		return add(index, docId, score, fields, null, payload);
	}

	@Override
	public RedisFuture<String> add(String index, K docId, double score, Map<K, V> fields, AddOptions options) {
		return add(index, docId, score, fields, options, null);
	}

	@Override
	public RedisFuture<String> add(String index, K docId, double score, Map<K, V> fields, AddOptions options,
			V payload) {
		return dispatch(commandBuilder.add(index, docId, score, fields, options, payload));
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
	public RedisFuture<String> drop(String index, DropOptions options) {
		return dispatch(commandBuilder.drop(index, options));
	}

	@Override
	public RedisFuture<List<Object>> ftInfo(String index) {
		return dispatch(commandBuilder.info(index));
	}

	@Override
	public RedisFuture<SearchResults<K, V>> search(String index, String query) {
		return search(index, query, SearchOptions.builder().build());
	}

	@Override
	public RedisFuture<SearchResults<K, V>> search(String index, String query, SearchOptions options) {
		return dispatch(commandBuilder.search(index, query, options));
	}

	@Override
	public RedisFuture<AggregateResults<K, V>> aggregate(String index, String query, AggregateOptions options) {
		return dispatch(commandBuilder.aggregate(index, query, options));
	}

	@Override
	public RedisFuture<AggregateWithCursorResults<K, V>> aggregate(String index, String query, AggregateOptions options,
			CursorOptions cursorOptions) {
		return dispatch(commandBuilder.aggregate(index, query, options, cursorOptions));
	}

	@Override
	public RedisFuture<AggregateWithCursorResults<K, V>> cursorRead(String index, long cursor) {
		return dispatch(commandBuilder.cursorRead(index, cursor, null));
	}

	@Override
	public RedisFuture<AggregateWithCursorResults<K, V>> cursorRead(String index, long cursor, long count) {
		return dispatch(commandBuilder.cursorRead(index, cursor, count));
	}

	@Override
	public RedisFuture<String> cursorDelete(String index, long cursor) {
		return dispatch(commandBuilder.cursorDelete(index, cursor));
	}

	@Override
	public RedisFuture<Long> sugadd(K key, V string, double score) {
		return sugadd(key, string, score, false, null);
	}

	@Override
	public RedisFuture<Long> sugadd(K key, V string, double score, boolean increment) {
		return sugadd(key, string, score, increment, null);
	}

	@Override
	public RedisFuture<Long> sugadd(K key, V string, double score, V payload) {
		return sugadd(key, string, score, false, payload);
	}

	@Override
	public RedisFuture<Long> sugadd(K key, V string, double score, boolean increment, V payload) {
		return dispatch(commandBuilder.sugadd(key, string, score, increment, payload));
	}

	@Override
	public RedisFuture<List<SuggestResult<V>>> sugget(K key, V prefix, SuggestGetOptions options) {
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
