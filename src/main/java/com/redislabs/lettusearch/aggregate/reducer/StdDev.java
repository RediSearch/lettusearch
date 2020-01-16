package com.redislabs.lettusearch.aggregate.reducer;

import static com.redislabs.lettusearch.protocol.CommandKeyword.STDDEV;

import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@SuperBuilder
public @Data class StdDev extends AbstractPropertyReducer {

	@Builder
	private StdDev(String as, String property) {
		super(as, property);
	}

	@Override
	protected <K, V> void buildFunction(RediSearchCommandArgs<K, V> args, String property) {
		args.add(STDDEV);
		args.add(1);
		args.addProperty(property);
	}

}
