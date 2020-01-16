package com.redislabs.lettusearch.aggregate.reducer;

import static com.redislabs.lettusearch.protocol.CommandKeyword.RANDOM_SAMPLE;

import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
public @Getter @Setter class RandomSample extends AbstractPropertyReducer {

	private int size;

	@Builder
	private RandomSample(String as, String property, int size) {
		super(as, property);
		this.size = size;
	}

	@Override
	protected <K, V> void buildFunction(RediSearchCommandArgs<K, V> args, String property) {
		args.add(RANDOM_SAMPLE);
		args.add(2);
		args.addProperty(property);
		args.add(size);
	}

}
