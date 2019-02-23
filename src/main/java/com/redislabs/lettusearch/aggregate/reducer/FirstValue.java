package com.redislabs.lettusearch.aggregate.reducer;

import static com.redislabs.lettusearch.CommandKeyword.ASC;
import static com.redislabs.lettusearch.CommandKeyword.BY;
import static com.redislabs.lettusearch.CommandKeyword.DESC;
import static com.redislabs.lettusearch.CommandKeyword.FIRST_VALUE;

import com.redislabs.lettusearch.RediSearchCommandArgs;
import com.redislabs.lettusearch.aggregate.Reducer;
import com.redislabs.lettusearch.aggregate.reducer.By.Order;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FirstValue extends Reducer {

	private final String property;
	private final By by;

	@Builder
	public FirstValue(String as, String property, By by) {
		super(as);
		this.property = property;
		this.by = by;
	}

	@Override
	protected <K, V> void buildFunction(RediSearchCommandArgs<K, V> args) {
		args.add(FIRST_VALUE);
		args.add(getNumberOfArgs());
		args.addProperty(property);
		if (by != null) {
			args.add(BY);
			args.addProperty(property);
			if (by.getOrder() != null) {
				args.add(by.getOrder() == Order.Asc ? ASC : DESC);
			}
		}
	}

	private int getNumberOfArgs() {
		int nargs = 1;
		if (by != null) {
			nargs += by.getOrder() == null ? 2 : 3;
		}
		return nargs;
	}

}
