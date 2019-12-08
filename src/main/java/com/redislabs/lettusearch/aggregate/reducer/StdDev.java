package com.redislabs.lettusearch.aggregate.reducer;

import static com.redislabs.lettusearch.CommandKeyword.STDDEV;

import com.redislabs.lettusearch.RediSearchCommandArgs;
import com.redislabs.lettusearch.aggregate.Reducer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
public @Data class StdDev extends Reducer {

	private final String property;

	@Override
	protected <K, V> void buildFunction(RediSearchCommandArgs<K, V> args) {
		args.add(STDDEV);
		args.add(1);
		args.addProperty(property);
	}

}
