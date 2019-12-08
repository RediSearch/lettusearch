package com.redislabs.lettusearch.aggregate.reducer;

import static com.redislabs.lettusearch.CommandKeyword.QUANTILE;

import com.redislabs.lettusearch.RediSearchCommandArgs;
import com.redislabs.lettusearch.aggregate.Reducer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
public @Data class Quantile extends Reducer {

	private final String property;
	private final double quantile;

	@Override
	protected <K, V> void buildFunction(RediSearchCommandArgs<K, V> args) {
		args.add(QUANTILE);
		args.add(2);
		args.addProperty(property);
		args.add(quantile);
	}

}
