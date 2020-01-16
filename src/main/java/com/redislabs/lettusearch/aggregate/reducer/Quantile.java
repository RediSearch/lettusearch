package com.redislabs.lettusearch.aggregate.reducer;

import static com.redislabs.lettusearch.protocol.CommandKeyword.QUANTILE;

import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public @Getter @Setter class Quantile extends AbstractPropertyReducer {

	private double quantile;

	@Builder
	private Quantile(String as, String property) {
		super(as, property);
	}

	@Override
	protected <K, V> void buildFunction(RediSearchCommandArgs<K, V> args, String property) {
		args.add(QUANTILE);
		args.add(2);
		args.addProperty(property);
		args.add(quantile);
	}

}
