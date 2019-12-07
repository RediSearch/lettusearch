package com.redislabs.lettusearch.search.field;

import static com.redislabs.lettusearch.CommandKeyword.NUMERIC;

import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Builder;

public class NumericField extends Field {

	@Builder
	public NumericField(String name, boolean sortable, boolean noIndex) {
		super(name, sortable, noIndex);
	}

	@Override
	protected <K, V> void buildField(RediSearchCommandArgs<K, V> args) {
		args.add(NUMERIC);
	}
}
