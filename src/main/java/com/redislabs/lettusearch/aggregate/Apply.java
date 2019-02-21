package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.CommandKeyword.*;

import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Apply implements Operation {

	private String expression;
	private String as;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		args.add(APPLY);
		args.add(expression);
		args.add(AS);
		args.add(as);
	}

}
