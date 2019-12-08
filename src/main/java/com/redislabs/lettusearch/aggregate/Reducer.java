package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.CommandKeyword.AS;
import static com.redislabs.lettusearch.CommandKeyword.REDUCE;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public abstract @Data class Reducer implements RediSearchArgument {

	private String as;

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		args.add(REDUCE);
		buildFunction(args);
		if (as != null) {
			args.add(AS);
			args.add(as);
		}
	}

	protected abstract <K, V> void buildFunction(RediSearchCommandArgs<K, V> args);
}
