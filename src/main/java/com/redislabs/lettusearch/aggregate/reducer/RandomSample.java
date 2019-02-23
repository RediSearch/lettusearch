package com.redislabs.lettusearch.aggregate.reducer;

import static com.redislabs.lettusearch.CommandKeyword.RANDOM_SAMPLE;

import com.redislabs.lettusearch.RediSearchCommandArgs;
import com.redislabs.lettusearch.aggregate.Reducer;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RandomSample extends Reducer {

	private final String property;
	private final int size;

	@Builder
	public RandomSample(String as, String property, int size) {
		super(as);
		this.property = property;
		this.size = size;
	}

	@Override
	protected <K, V> void buildFunction(RediSearchCommandArgs<K, V> args) {
		args.add(RANDOM_SAMPLE);
		args.add(2);
		args.addProperty(property);
		args.add(size);
	}

}
