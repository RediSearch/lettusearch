package com.redislabs.lettusearch.aggregate.reduce;

import static com.redislabs.lettusearch.CommandKeyword.FIRST_VALUE;

import com.redislabs.lettusearch.aggregate.Function;
import com.redislabs.lettusearch.aggregate.PropertyArgument;

import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class FirstValue extends PropertyArgument implements Function {

	private String property;
	private By by;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		args.add(FIRST_VALUE);
		args.add(getNumberOfArgs());
		args.add(prefix(property));
		if (by != null) {
			by.build(args);
		}
	}

	private int getNumberOfArgs() {
		int nargs = 1;
		if (by != null) {
			nargs += by.getNumberOfArgs();
		}
		return nargs;

	}

}
