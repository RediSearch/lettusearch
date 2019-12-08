package com.redislabs.lettusearch.search.field;

import static com.redislabs.lettusearch.CommandKeyword.NOINDEX;
import static com.redislabs.lettusearch.CommandKeyword.SORTABLE;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.RediSearchCommandArgs;

import io.lettuce.core.internal.LettuceAssert;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public abstract class Field implements RediSearchArgument {

	static final String MUST_NOT_BE_EMPTY = "must not be empty";
	static final String MUST_NOT_BE_NULL = "must not be null";

	@Getter
	private String name;
	@Getter
	@Setter
	private boolean sortable;
	@Getter
	@Setter
	private boolean noIndex;

	protected Field(String name) {
		super();
		this.name = name;
	}

	public static TextField text(String name) {
		return new TextField(name);
	}

	public static GeoField geo(String name) {
		return new GeoField(name);
	}

	public static NumericField numeric(String name) {
		return new NumericField(name);
	}

	public static TagField tag(String name) {
		return new TagField(name);
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