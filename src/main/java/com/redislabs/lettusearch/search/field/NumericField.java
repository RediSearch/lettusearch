package com.redislabs.lettusearch.search.field;

import static com.redislabs.lettusearch.CommandKeyword.NUMERIC;

import com.redislabs.lettusearch.RediSearchCommandArgs;

public class NumericField extends Field {

	public NumericField(String name) {
		super(name);
	}

	@Override
	protected <K, V> void buildField(RediSearchCommandArgs<K, V> args) {
		args.add(NUMERIC);
	}
}
