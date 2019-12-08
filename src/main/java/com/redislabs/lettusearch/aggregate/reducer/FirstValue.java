package com.redislabs.lettusearch.aggregate.reducer;

import static com.redislabs.lettusearch.CommandKeyword.ASC;
import static com.redislabs.lettusearch.CommandKeyword.BY;
import static com.redislabs.lettusearch.CommandKeyword.DESC;
import static com.redislabs.lettusearch.CommandKeyword.FIRST_VALUE;

import com.redislabs.lettusearch.RediSearchCommandArgs;
import com.redislabs.lettusearch.aggregate.Reducer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
public @Data class FirstValue extends Reducer {

	private final String property;
	private final By by;

	@Override
	protected <K, V> void buildFunction(RediSearchCommandArgs<K, V> args) {
		args.add(FIRST_VALUE);
		args.add(getNumberOfArgs());
		args.addProperty(property);
		if (by != null) {
			args.add(BY);
			args.addProperty(property);
			if (by.order() != null) {
				args.add(by.order() == Order.Asc ? ASC : DESC);
			}
		}
	}

	private int getNumberOfArgs() {
		int nargs = 1;
		if (by != null) {
			nargs += by.order() == null ? 2 : 3;
		}
		return nargs;
	}

}
