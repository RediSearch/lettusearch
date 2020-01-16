package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.protocol.CommandKeyword.AS;
import static com.redislabs.lettusearch.protocol.CommandKeyword.REDUCE;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@AllArgsConstructor
public abstract class Reducer implements RediSearchArgument {

	@Getter
	@Setter
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
