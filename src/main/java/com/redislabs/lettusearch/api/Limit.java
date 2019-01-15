package com.redislabs.lettusearch.api;

import io.lettuce.core.CompositeArgument;
import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder
public class Limit implements CompositeArgument {

	@Default
	private long offset = 0;
	@Default
	private long num = 10;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		args.add(offset);
		args.add(num);
	}

}
