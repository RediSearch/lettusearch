package com.redislabs.lettusearch.aggregate.reducer;

import static com.redislabs.lettusearch.CommandKeyword.STDDEV;

import com.redislabs.lettusearch.RediSearchCommandArgs;
import com.redislabs.lettusearch.aggregate.Reducer;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StdDev extends Reducer {

	private final String property;

	@Builder
	public StdDev(String as, String property) {
		super(as);
		this.property = property;
	}

	@Override
	protected <K, V> void buildFunction(RediSearchCommandArgs<K, V> args) {
		args.add(STDDEV);
		args.add(1);
		args.addProperty(property);
	}

}
