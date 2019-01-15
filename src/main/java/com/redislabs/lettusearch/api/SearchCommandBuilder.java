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
package com.redislabs.lettusearch.api;

import static com.redislabs.lettusearch.api.CommandType.*;

import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.internal.LettuceAssert;
import io.lettuce.core.output.CommandOutput;
import io.lettuce.core.output.IntegerOutput;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.protocol.BaseRedisCommandBuilder;
import io.lettuce.core.protocol.Command;
import io.lettuce.core.protocol.CommandArgs;

/**
 * Dedicated pub/sub command builder to build pub/sub commands.
 *
 * @author Mark Paluch
 * @since 4.2
 */
public class SearchCommandBuilder<K, V> extends BaseRedisCommandBuilder<K, V> {

	static final String MUST_NOT_BE_NULL = "must not be null";
	static final String MUST_NOT_BE_EMPTY = "must not be empty";

	public SearchCommandBuilder(RedisCodec<K, V> codec) {
		super(codec);
	}

	public Command<K, V, String> add(String index, Document document) {
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		LettuceAssert.notEmpty(index, "index " + MUST_NOT_BE_EMPTY);
		LettuceAssert.notNull(document, "document " + MUST_NOT_BE_NULL);
		CommandArgs<K, V> args = new SearchCommandArgs<>(codec).add(index);
		document.build(args);
		return createCommand(ADD, new StatusOutput<>(codec), args);
	}

	public Command<K, V, String> create(String index, Schema schema) {
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		LettuceAssert.notEmpty(index, "index " + MUST_NOT_BE_EMPTY);
		LettuceAssert.notNull(schema, "schema " + MUST_NOT_BE_NULL);
		CommandArgs<K, V> args = new SearchCommandArgs<>(codec).add(index);
		schema.build(args);
		return createCommand(CREATE, new StatusOutput<>(codec), args);
	}

	public Command<K, V, String> drop(String index, DropOptions options) {
		LettuceAssert.notNull(index, "index " + MUST_NOT_BE_NULL);
		CommandArgs<K, V> args = new SearchCommandArgs<>(codec).add(index);
		options.build(args);
		return createCommand(DROP, new StatusOutput<>(codec), args);
	}

	public Command<K, V, Long> add(String key, Suggestion suggestion) {
		LettuceAssert.notNull(key, "key " + MUST_NOT_BE_NULL);
		LettuceAssert.notNull(suggestion, "suggestion " + MUST_NOT_BE_NULL);
		CommandArgs<K, V> args = new SearchCommandArgs<>(codec).add(key);
		suggestion.build(args);
		return createCommand(SUGADD, new IntegerOutput<>(codec), args);
	}

	protected <T> Command<K, V, T> createCommand(CommandType type, CommandOutput<K, V, T> output,
			CommandArgs<K, V> args) {
		return new Command<K, V, T>(type, output, args);
	}

}
