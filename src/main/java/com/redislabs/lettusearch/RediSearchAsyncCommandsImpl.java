package com.redislabs.lettusearch;

import java.util.List;
import java.util.Map;

import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.AggregateResults;
import com.redislabs.lettusearch.search.AddOptions;
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

	private final RediSearchCommandBuilder<K, V> commandBuilder;

	public RediSearchAsyncCommandsImpl(StatefulRediSearchConnection<K, V> connection, RedisCodec<K, V> codec) {
		super(connection, codec);
		this.commandBuilder = new RediSearchCommandBuilder<>(codec);
	}

	@Override
	public StatefulRediSearchConnection<K, V> getStatefulConnection() {
		return (StatefulRediSearchConnection<K, V>) super.getStatefulConnection();
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
		return dispatch(commandBuilder.create(index, schema));
	}

	@Override
	public RedisFuture<String> drop(String index, DropOptions options) {
		return dispatch(commandBuilder.drop(index, options));
	}

	@Override
	public RedisFuture<List<Object>> indexInfo(String index) {
		return dispatch(commandBuilder.indexInfo(index));
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
	public RedisFuture<Map<K, V>> get(String index, K docId) {
		return dispatch(commandBuilder.get(index, docId));
	}

	@Override
	public RedisFuture<Boolean> del(String index, K docId, boolean deleteDoc) {
		return dispatch(commandBuilder.del(index, docId, deleteDoc));
	}

	@Override
	public RedisFuture<String> alter(String index, K field, FieldOptions options) {
		return dispatch(commandBuilder.alter(index, field, options));
	}

}
