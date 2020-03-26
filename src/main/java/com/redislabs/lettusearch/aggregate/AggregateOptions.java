package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.protocol.CommandKeyword.LOAD;

import java.util.List;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Builder
public @Data class AggregateOptions implements RediSearchArgument {

	@Singular
	private List<String> loads;
	@Singular
	private List<Operation> operations;

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		if (!loads.isEmpty()) {
			args.add(LOAD);
			args.add(loads.size());
			for (String load : loads) {
				args.addProperty(load);
			}
		}
		for (Operation operation : operations) {
			operation.build(args);
		}
	}

}
