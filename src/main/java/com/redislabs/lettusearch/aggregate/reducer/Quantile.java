package com.redislabs.lettusearch.aggregate.reducer;

import static com.redislabs.lettusearch.CommandKeyword.QUANTILE;

import com.redislabs.lettusearch.RediSearchCommandArgs;
import com.redislabs.lettusearch.aggregate.Reducer;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Quantile extends Reducer {

	private final String property;
	private final double quantile;

	@Builder
	public Quantile(String as, String property, double quantile) {
		super(as);
		this.property = property;
		this.quantile = quantile;
	}

	@Override
	protected <K, V> void buildFunction(RediSearchCommandArgs<K, V> args) {
		args.add(QUANTILE);
		args.add(2);
		args.addProperty(property);
		args.add(quantile);
	}

}
