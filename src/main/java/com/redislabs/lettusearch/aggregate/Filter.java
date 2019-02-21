package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.CommandKeyword.*;

import io.lettuce.core.protocol.CommandArgs;

public class Filter implements Operation {

	private String expression;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		args.add(FILTER);
		args.add(expression);
	}

}
