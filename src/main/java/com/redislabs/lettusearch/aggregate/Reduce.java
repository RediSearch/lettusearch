package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.CommandKeyword.*;

import io.lettuce.core.CompositeArgument;
import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Reduce implements CompositeArgument {

	private Function function;
	private String as;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		args.add(REDUCE);
		function.build(args);
		if (as!=null) {
			args.add(AS);
			args.add(as);
		}
	}

}
