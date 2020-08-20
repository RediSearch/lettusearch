package com.redislabs.lettusearch.index.field;

import static com.redislabs.lettusearch.protocol.CommandKeyword.NOINDEX;
import static com.redislabs.lettusearch.protocol.CommandKeyword.SORTABLE;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import io.lettuce.core.internal.LettuceAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class Field<K> implements RediSearchArgument {

	static final String MUST_NOT_BE_EMPTY = "must not be empty";
	static final String MUST_NOT_BE_NULL = "must not be null";

	private K name;
	private boolean sortable;
	private boolean noIndex;

	protected Field(K name) {
		this.name = name;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void build(RediSearchCommandArgs args) {
		LettuceAssert.notNull(name, "name " + MUST_NOT_BE_NULL);
		args.addKey(name);
		buildField(args);
		if (sortable) {
			args.add(SORTABLE);
		}
		if (noIndex) {
			args.add(NOINDEX);
		}
	}

	@SuppressWarnings("rawtypes")
	protected abstract void buildField(RediSearchCommandArgs args);

}