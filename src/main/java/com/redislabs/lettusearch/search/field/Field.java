package com.redislabs.lettusearch.search.field;

import static com.redislabs.lettusearch.CommandKeyword.NOINDEX;
import static com.redislabs.lettusearch.CommandKeyword.SORTABLE;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.RediSearchCommandArgs;

import io.lettuce.core.internal.LettuceAssert;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
public abstract @Data class Field implements RediSearchArgument {

	static final String MUST_NOT_BE_EMPTY = "must not be empty";
	static final String MUST_NOT_BE_NULL = "must not be null";

	private String name;
	private boolean sortable;
	private boolean noIndex;

	public static TextField text(String name) {
		return TextField.builder().name(name).build();
	}

	public static GeoField geo(String name) {
		return GeoField.builder().name(name).build();
	}

	public static NumericField numeric(String name) {
		return NumericField.builder().name(name).build();
	}

	public static TagField tag(String name) {
		return TagField.builder().name(name).build();
	}

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		LettuceAssert.notNull(name, "name " + MUST_NOT_BE_NULL);
		LettuceAssert.notEmpty(name, "name " + MUST_NOT_BE_EMPTY);
		args.add(name);
		buildField(args);
		if (sortable) {
			args.add(SORTABLE);
		}
		if (noIndex) {
			args.add(NOINDEX);
		}
	}

	protected abstract <K, V> void buildField(RediSearchCommandArgs<K, V> args);

}