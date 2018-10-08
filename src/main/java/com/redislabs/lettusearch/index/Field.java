package com.redislabs.lettusearch.index;

import static com.redislabs.lettusearch.index.CommandKeyword.NOINDEX;
import static com.redislabs.lettusearch.index.CommandKeyword.SORTABLE;

import io.lettuce.core.CompositeArgument;
import io.lettuce.core.protocol.CommandArgs;
import lombok.Data;

@Data
public class Field implements CompositeArgument {

	private String name;
	private boolean sortable;
	private boolean noIndex;

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