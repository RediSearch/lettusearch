package com.redislabs.lettusearch.search;

import static com.redislabs.lettusearch.CommandKeyword.LIMIT;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder
public class Limit implements RediSearchArgument {

	public final static long DEFAULT_OFFSET = 0;
	public final static long DEFAULT_NUM = 10;

	@Default
	private long offset = DEFAULT_OFFSET;
	@Default
	private long num = DEFAULT_NUM;

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		args.add(LIMIT);
		args.add(offset);
		args.add(num);
	}

}
