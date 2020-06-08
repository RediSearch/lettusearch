package com.redislabs.lettusearch.index;

import static com.redislabs.lettusearch.protocol.CommandKeyword.SCHEMA;

import java.util.List;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.index.field.Field;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import io.lettuce.core.internal.LettuceAssert;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
public class Schema implements RediSearchArgument {

	static final String MUST_NOT_BE_EMPTY = "must not be empty";

	@Singular
	private List<Field> fields;

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		LettuceAssert.isTrue(!fields.isEmpty(), "fields " + MUST_NOT_BE_EMPTY);
		args.add(SCHEMA);
		fields.forEach(field -> field.build(args));
	}

}
