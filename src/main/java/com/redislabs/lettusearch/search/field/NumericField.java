package com.redislabs.lettusearch.search.field;

import static com.redislabs.lettusearch.protocol.CommandKeyword.NUMERIC;

import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;

public class NumericField extends Field {

	@Builder
	private NumericField(String name) {
		super(name);
	}

	@Override
	protected <K, V> void buildField(RediSearchCommandArgs<K, V> args) {
		args.add(NUMERIC);
	}
}
