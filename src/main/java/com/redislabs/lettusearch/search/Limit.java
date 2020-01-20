package com.redislabs.lettusearch.search;

import static com.redislabs.lettusearch.protocol.CommandKeyword.LIMIT;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;

@Builder
public @Data class Limit implements RediSearchArgument {

	public final static long DEFAULT_OFFSET = 0;
	public final static long DEFAULT_NUM = 10;

	@Builder.Default
	private long offset = DEFAULT_OFFSET;
	@Builder.Default
	private long num = DEFAULT_NUM;

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		args.add(LIMIT);
		args.add(offset);
		args.add(num);
	}

}
