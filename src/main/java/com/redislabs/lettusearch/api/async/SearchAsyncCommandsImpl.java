/*
 * Copyright 2011-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redislabs.lettusearch.api.async;

import com.redislabs.lettusearch.StatefulSearchConnection;
import com.redislabs.lettusearch.api.Document;
import com.redislabs.lettusearch.api.DropOptions;
import com.redislabs.lettusearch.api.Schema;
import com.redislabs.lettusearch.api.SearchCommandBuilder;
import com.redislabs.lettusearch.api.Suggestion;

import io.lettuce.core.RedisAsyncCommandsImpl;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.codec.RedisCodec;

public class SearchAsyncCommandsImpl<K, V> extends RedisAsyncCommandsImpl<K, V> implements SearchAsyncCommands<K, V> {

	private final SearchCommandBuilder<K, V> commandBuilder;

	public SearchAsyncCommandsImpl(StatefulSearchConnection<K, V> connection, RedisCodec<K, V> codec) {
		super(connection, codec);
		this.commandBuilder = new SearchCommandBuilder<>(codec);
	}

	@Override
	public StatefulSearchConnection<K, V> getStatefulConnection() {
		return (StatefulSearchConnection<K, V>) super.getStatefulConnection();
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
	public RedisFuture<Long> add(String key, Suggestion suggestion) {
		return dispatch(commandBuilder.add(key, suggestion));
	}
}
