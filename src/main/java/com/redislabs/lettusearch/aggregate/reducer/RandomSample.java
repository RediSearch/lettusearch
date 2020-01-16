package com.redislabs.lettusearch.aggregate.reducer;

import static com.redislabs.lettusearch.protocol.CommandKeyword.RANDOM_SAMPLE;

import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@SuperBuilder
public @Data class RandomSample extends AbstractPropertyReducer {

	@Builder
	private RandomSample(String as, String property) {
		super(as, property);
	}

	private int size;

	@Override
	protected <K, V> void buildFunction(RediSearchCommandArgs<K, V> args, String property) {
		args.add(RANDOM_SAMPLE);
		args.add(2);
		args.addProperty(property);
		args.add(size);
	}

}
