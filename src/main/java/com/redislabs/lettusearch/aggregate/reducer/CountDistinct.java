package com.redislabs.lettusearch.aggregate.reducer;

import static com.redislabs.lettusearch.protocol.CommandKeyword.COUNT_DISTINCT;

import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;

public class CountDistinct extends AbstractPropertyReducer {

	@Builder
	private CountDistinct(String as, String property) {
		super(as, property);
	}

	@Override
	protected <K, V> void buildFunction(RediSearchCommandArgs<K, V> args, String property) {
		args.add(COUNT_DISTINCT);
		args.add(1);
		args.addProperty(property);
	}

}
