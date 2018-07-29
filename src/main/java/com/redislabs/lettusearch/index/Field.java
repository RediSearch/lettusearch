package com.redislabs.lettusearch.index;

import static com.redislabs.lettusearch.index.CommandKeyword.NOINDEX;
import static com.redislabs.lettusearch.index.CommandKeyword.SORTABLE;

import io.lettuce.core.CompositeArgument;
import io.lettuce.core.protocol.CommandArgs;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Accessors(chain = true)
public abstract class Field implements CompositeArgument {

	@NonNull
	String name;
	boolean sortable;
	boolean noIndex;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		args.add(name);
		buildField(args);
	}

	protected <K, V> void buildField(CommandArgs<K, V> args) {
		if (sortable) {
			args.add(SORTABLE);
		}
		if (noIndex) {
			args.add(NOINDEX);
		}
	}

}
