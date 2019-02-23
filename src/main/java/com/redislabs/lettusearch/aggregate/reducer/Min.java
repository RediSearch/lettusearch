package com.redislabs.lettusearch.aggregate.reducer;

import static com.redislabs.lettusearch.CommandKeyword.MIN;

import com.redislabs.lettusearch.RediSearchCommandArgs;
import com.redislabs.lettusearch.aggregate.Reducer;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Min extends Reducer {

	private final String property;

	@Builder
	public Min(String as, String property) {
		super(as);
		this.property = property;
	}

	@Override
	protected <K, V> void buildFunction(RediSearchCommandArgs<K, V> args) {
		args.add(MIN);
		args.add(1);
		args.addProperty(property);
	}

}
