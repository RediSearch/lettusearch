package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.CommandKeyword.APPLY;
import static com.redislabs.lettusearch.CommandKeyword.AS;

import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Apply implements Operation {

	private String expression;
	private String as;

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		args.add(APPLY);
		args.add(expression);
		args.add(AS);
		args.add(as);
	}

}
