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
package com.redislabs.lettusearch;

import static com.redislabs.lettusearch.CommandKeyword.*;

import java.util.ArrayList;
import java.util.List;

import io.lettuce.core.CompositeArgument;
import io.lettuce.core.internal.LettuceAssert;
import io.lettuce.core.protocol.CommandArgs;

public class SchemaArgs implements CompositeArgument {

	private List<Field> fields;

	public SchemaArgs fields(Field... fields) {
		LettuceAssert.notNull(fields, "Fields must not be null");
		this.fields = new ArrayList<>(fields.length);
		for (Field field : fields) {
			this.fields.add(field);
		}
		return this;
	}

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		if (fields != null && !fields.isEmpty()) {
			args.add(SCHEMA);
			for (Field field : fields) {
				args.add(field.getName());
				switch (field.getType()) {
				case TEXT:
					args.add(TEXT);
					if (field.getWeight() != null) {
						args.add(WEIGHT);
						args.add(field.getWeight());
					}
					break;
				case GEO:
					args.add(GEO);
					break;
				case NUMERIC:
					args.add(NUMERIC);
					break;
				}
				if (field.isSortable()) {
					args.add(SORTABLE);
				}
				if (field.isNoStem()) {
					args.add(NOSTEM);
				}
				if (field.isNoIndex()) {
					args.add(NOINDEX);
				}
			}
		}
	}
}
