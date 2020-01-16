package com.redislabs.lettusearch.aggregate.reducer;

import static com.redislabs.lettusearch.protocol.CommandKeyword.ASC;
import static com.redislabs.lettusearch.protocol.CommandKeyword.BY;
import static com.redislabs.lettusearch.protocol.CommandKeyword.DESC;
import static com.redislabs.lettusearch.protocol.CommandKeyword.FIRST_VALUE;

import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@SuperBuilder
public @Data class FirstValue extends AbstractPropertyReducer {

	private By by;

	@Builder
	private FirstValue(String as, String property, By by) {
		super(as, property);
		this.by = by;
	}

	@Override
	protected <K, V> void buildFunction(RediSearchCommandArgs<K, V> args, String property) {
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
