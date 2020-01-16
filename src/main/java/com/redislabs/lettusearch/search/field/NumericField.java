package com.redislabs.lettusearch.search.field;

import static com.redislabs.lettusearch.protocol.CommandKeyword.NUMERIC;

import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@SuperBuilder
public @Data class NumericField extends Field {

	@Builder
	private NumericField(String name) {
		super(name);
	}

	@Override
	protected <K, V> void buildField(RediSearchCommandArgs<K, V> args) {
		args.add(NUMERIC);
	}
}
