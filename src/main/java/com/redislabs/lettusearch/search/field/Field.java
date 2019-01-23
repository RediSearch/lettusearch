package com.redislabs.lettusearch.search.field;

import static com.redislabs.lettusearch.CommandKeyword.NOINDEX;
import static com.redislabs.lettusearch.CommandKeyword.SORTABLE;

import io.lettuce.core.CompositeArgument;
import io.lettuce.core.internal.LettuceAssert;
import io.lettuce.core.protocol.CommandArgs;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Field implements CompositeArgument {

	static final String MUST_NOT_BE_EMPTY = "must not be empty";
	static final String MUST_NOT_BE_NULL = "must not be null";

	private final String name;
	private final boolean sortable;
	private final boolean noIndex;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
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

	protected abstract <K, V> void buildField(CommandArgs<K, V> args);

}