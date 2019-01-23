package com.redislabs.lettusearch;

import java.util.List;
import java.util.Map;

import com.redislabs.lettusearch.search.AddOptions;
import com.redislabs.lettusearch.search.DropOptions;
import com.redislabs.lettusearch.search.Schema;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.suggest.SuggestGetOptions;
import com.redislabs.lettusearch.suggest.SuggestResult;
import com.redislabs.lettusearch.suggest.SuggestAddOptions;

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
	public RedisFuture<String> add(String index, K docId, double score, Map<K, V> fields, AddOptions options) {
		return dispatch(commandBuilder.add(index, docId, score, fields, options));
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
	public RedisFuture<SearchResults<K, V>> search(String index, String query, SearchOptions options) {
		return dispatch(commandBuilder.search(index, query, options));
	}

	@Override
	public RedisFuture<Long> sugadd(K key, V string, double score, SuggestAddOptions options) {
		return dispatch(commandBuilder.sugadd(key, string, score, options));
	}

	@Override
	public RedisFuture<List<SuggestResult<V>>> sugget(K key, V prefix, SuggestGetOptions options) {
		return dispatch(commandBuilder.sugget(key, prefix, options));
	}

}
