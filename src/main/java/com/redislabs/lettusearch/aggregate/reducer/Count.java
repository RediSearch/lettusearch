package com.redislabs.lettusearch.aggregate.reducer;

import static com.redislabs.lettusearch.CommandKeyword.COUNT;

import com.redislabs.lettusearch.RediSearchCommandArgs;
import com.redislabs.lettusearch.aggregate.Reducer;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Count extends Reducer {

	@Builder
	public Count(String as) {
		super(as);
	}

	@Override
	protected <K, V> void buildFunction(RediSearchCommandArgs<K, V> args) {
		args.add(COUNT);
		args.add(0);
	}

}
