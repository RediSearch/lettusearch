package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.CommandKeyword.FILTER;

import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Filter implements Operation {

	private String expression;

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		args.add(FILTER);
		args.add(expression);
	}

}
