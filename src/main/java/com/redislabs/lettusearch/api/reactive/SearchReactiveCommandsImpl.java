package com.redislabs.lettusearch.api.reactive;

import com.redislabs.lettusearch.StatefulSearchConnection;
import com.redislabs.lettusearch.api.Document;
import com.redislabs.lettusearch.api.DropOptions;
import com.redislabs.lettusearch.api.Schema;
import com.redislabs.lettusearch.api.SearchCommandBuilder;
import com.redislabs.lettusearch.api.Suggestion;

import io.lettuce.core.RedisReactiveCommandsImpl;
import io.lettuce.core.codec.RedisCodec;
import reactor.core.publisher.Mono;

public class SearchReactiveCommandsImpl<K, V> extends RedisReactiveCommandsImpl<K, V>
		implements SearchReactiveCommands<K, V> {

	private final SearchCommandBuilder<K, V> commandBuilder;

	public SearchReactiveCommandsImpl(StatefulSearchConnection<K, V> connection, RedisCodec<K, V> codec) {
		super(connection, codec);
		this.commandBuilder = new SearchCommandBuilder<>(codec);
	}

	@Override
	public Mono<String> add(String index, Document document) {
		return createMono(() -> commandBuilder.add(index, document));
	}

	@Override
	public Mono<String> create(String index, Schema schema) {
		return createMono(() -> commandBuilder.create(index, schema));
	}

	@Override
	public Mono<String> drop(String index, DropOptions options) {
		return createMono(() -> commandBuilder.drop(index, options));
	}

	@Override
	public Mono<Long> add(String key, Suggestion suggestion) {
		return createMono(() -> commandBuilder.add(key, suggestion));
	}

	@Override
	public StatefulSearchConnection<K, V> getStatefulConnection() {
		return (StatefulSearchConnection<K, V>) super.getStatefulConnection();
	}

}
