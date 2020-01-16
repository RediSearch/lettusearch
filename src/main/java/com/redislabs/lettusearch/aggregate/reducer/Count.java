package com.redislabs.lettusearch.aggregate.reducer;

import static com.redislabs.lettusearch.protocol.CommandKeyword.COUNT;

import com.redislabs.lettusearch.aggregate.Reducer;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;

public class Count extends Reducer {

	@Builder
	private Count(String as) {
		super(as);
	}

	@Override
	protected <K, V> void buildFunction(RediSearchCommandArgs<K, V> args) {
		args.add(COUNT);
		args.add(0);
	}

}
