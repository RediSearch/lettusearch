package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.protocol.CommandKeyword.LIMIT;

import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Limit implements Operation {

	private long offset;
	private long num;

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		args.add(LIMIT);
		args.add(offset);
		args.add(num);
	}

}
