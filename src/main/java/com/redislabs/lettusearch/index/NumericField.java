package com.redislabs.lettusearch.index;

import static com.redislabs.lettusearch.index.CommandKeyword.NUMERIC;

import io.lettuce.core.protocol.CommandArgs;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NumericField extends Field {

	@Override
	protected <K, V> void buildField(CommandArgs<K, V> args) {
		args.add(NUMERIC);
		super.buildField(args);
	}
}
