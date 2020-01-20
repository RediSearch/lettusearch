package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.protocol.CommandKeyword.APPLY;
import static com.redislabs.lettusearch.protocol.CommandKeyword.AS;

import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;

@Builder
public @Data class Apply implements Operation {

	private String expression;
	private String as;

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		args.add(APPLY);
		args.add(expression);
		args.add(AS);
		args.add(as);
	}

}
