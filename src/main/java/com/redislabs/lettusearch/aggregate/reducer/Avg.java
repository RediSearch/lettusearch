package com.redislabs.lettusearch.aggregate.reducer;

import static com.redislabs.lettusearch.protocol.CommandKeyword.AVG;

import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@SuperBuilder
public class Avg extends AbstractPropertyReducer {

	@Builder
	private Avg(String as, String property) {
		super(as, property);
	}

	@Override
	protected <K, V> void buildFunction(RediSearchCommandArgs<K, V> args, String property) {
		args.add(AVG);
		args.add(1);
		args.addProperty(property);
	}

}
