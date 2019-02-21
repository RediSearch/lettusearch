package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.CommandKeyword.LIMIT;

import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Limit implements Operation {

	private long offset;
	private long num;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		args.add(LIMIT);
		args.add(offset);
		args.add(num);
	}

}
