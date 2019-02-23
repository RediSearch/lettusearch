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

	@Default
	private long offset = 0;
	@Default
	private long num = 10;

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		args.add(LIMIT);
		args.add(offset);
		args.add(num);
	}

}
