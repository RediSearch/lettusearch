package com.redislabs.lettusearch.api;

import static com.redislabs.lettusearch.api.CommandKeyword.GEO;
import static com.redislabs.lettusearch.api.CommandKeyword.NOINDEX;
import static com.redislabs.lettusearch.api.CommandKeyword.NUMERIC;
import static com.redislabs.lettusearch.api.CommandKeyword.SORTABLE;
import static com.redislabs.lettusearch.api.CommandKeyword.TEXT;

import io.lettuce.core.CompositeArgument;
import io.lettuce.core.internal.LettuceAssert;
import io.lettuce.core.protocol.CommandArgs;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Field implements CompositeArgument {

	static final String MUST_NOT_BE_EMPTY = "must not be empty";
	static final String MUST_NOT_BE_NULL = "must not be null";

	private final String name;
	private final FieldType type;
	private boolean sortable;
	private boolean noIndex;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		LettuceAssert.notNull(name, "name " + MUST_NOT_BE_NULL);
		LettuceAssert.notEmpty(name, "name " + MUST_NOT_BE_EMPTY);
		args.add(name);
		switch (type) {
		case Numeric:
			args.add(NUMERIC);
			break;
		case Geo:
			args.add(GEO);
			break;
		case Text:
			args.add(TEXT);
		}
		buildField(args);
		if (sortable) {
			args.add(SORTABLE);
		}
		if (noIndex) {
			args.add(NOINDEX);
		}
	}

	protected <K, V> void buildField(CommandArgs<K, V> args) {
		// do nothing
	}

}