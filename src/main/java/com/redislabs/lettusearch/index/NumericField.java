package com.redislabs.lettusearch.index;

import static com.redislabs.lettusearch.index.CommandKeyword.NUMERIC;

import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NumericField extends Field {

	@Builder
	public NumericField(String name, boolean sortable, boolean noindex) {
		super(name, sortable, noindex);
	}

	@Override
	protected <K, V> void buildField(CommandArgs<K, V> args) {
		args.add(NUMERIC);
		super.buildField(args);
	}
}
