package com.redislabs.lettusearch.search;

import static com.redislabs.lettusearch.CommandKeyword.SCHEMA;

import java.util.ArrayList;
import java.util.List;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.RediSearchCommandArgs;
import com.redislabs.lettusearch.search.field.Field;

import io.lettuce.core.internal.LettuceAssert;

public class Schema implements RediSearchArgument {

	static final String MUST_NOT_BE_EMPTY = "must not be empty";

	private List<Field> fields = new ArrayList<>();

	public Schema field(Field field) {
		fields.add(field);
		return this;
	}

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		LettuceAssert.isTrue(!fields.isEmpty(), "fields " + MUST_NOT_BE_EMPTY);
		args.add(SCHEMA);
		fields.forEach(field -> field.build(args));
	}

}
