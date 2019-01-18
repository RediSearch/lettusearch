package com.redislabs.lettusearch;

import java.util.List;

import com.redislabs.lettusearch.search.Document;
import com.redislabs.lettusearch.search.DropOptions;
import com.redislabs.lettusearch.search.Schema;
import com.redislabs.lettusearch.search.SearchOptions;
import com.redislabs.lettusearch.search.SearchResults;
import com.redislabs.lettusearch.suggest.GetOptions;
import com.redislabs.lettusearch.suggest.SuggestResult;
import com.redislabs.lettusearch.suggest.Suggestion;

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
	public RedisFuture<String> add(String index, Document document) {
		return dispatch(commandBuilder.add(index, document));
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
	public RedisFuture<Long> add(String key, Suggestion suggestion) {
		return dispatch(commandBuilder.add(key, suggestion));
	}

	@Override
	public RedisFuture<List<SuggestResult<V>>> get(K key, V prefix, GetOptions options) {
		return dispatch(commandBuilder.get(key, prefix, options));
	}

}
