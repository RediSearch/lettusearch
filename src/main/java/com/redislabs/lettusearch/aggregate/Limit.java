package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.protocol.CommandKeyword.LIMIT;

import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Builder
public @Data class Limit implements Operation {

	private long offset;
	private long num;

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		args.add(LIMIT);
		args.add(offset);
		args.add(num);
	}

}
