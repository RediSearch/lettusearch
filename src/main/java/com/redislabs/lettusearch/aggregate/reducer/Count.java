package com.redislabs.lettusearch.aggregate.reducer;

import static com.redislabs.lettusearch.protocol.CommandKeyword.COUNT;

import com.redislabs.lettusearch.aggregate.Reducer;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@SuperBuilder
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
