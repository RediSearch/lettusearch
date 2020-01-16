package com.redislabs.lettusearch.aggregate.reducer;

import static com.redislabs.lettusearch.protocol.CommandKeyword.MIN;

import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;

public class Min extends AbstractPropertyReducer {

	@Builder
	private Min(String as, String property) {
		super(as, property);
	}

	@Override
	protected <K, V> void buildFunction(RediSearchCommandArgs<K, V> args, String property) {
		args.add(MIN);
		args.add(1);
		args.addProperty(property);
	}

}
