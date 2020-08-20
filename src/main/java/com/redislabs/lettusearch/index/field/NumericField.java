package com.redislabs.lettusearch.index.field;

import static com.redislabs.lettusearch.protocol.CommandKeyword.NUMERIC;

import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;

public class NumericField<K> extends Field<K> {

	@Builder
	private NumericField(K name, boolean sortable, boolean noIndex) {
		super(name, sortable, noIndex);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void buildField(RediSearchCommandArgs args) {
		args.add(NUMERIC);
	}
}
