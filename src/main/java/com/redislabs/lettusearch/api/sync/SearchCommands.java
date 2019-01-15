/*
 * Copyright 2017-2018 the original author or authors.
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
package com.redislabs.lettusearch.api.sync;

import com.redislabs.lettusearch.api.Document;
import com.redislabs.lettusearch.api.DropOptions;
import com.redislabs.lettusearch.api.Schema;
import com.redislabs.lettusearch.api.Suggestion;

import io.lettuce.core.api.sync.RedisCommands;

/**
 * Synchronously executed commands for Strings.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Julien Ruaux
 */
public interface SearchCommands<K, V> extends RedisCommands<K, V> {

	String add(String index, Document document);

	String create(String index, Schema schema);

	String drop(String index, DropOptions options);

	Long add(String key, Suggestion suggestion);

}
