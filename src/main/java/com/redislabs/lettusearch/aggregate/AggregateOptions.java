package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.protocol.CommandKeyword.LOAD;
import static com.redislabs.lettusearch.protocol.CommandKeyword.VERBATIM;
import static com.redislabs.lettusearch.protocol.CommandKeyword.WITHSCHEMA;

import java.util.List;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Builder
public @Data class AggregateOptions implements RediSearchArgument {

	private boolean withSchema;
	private boolean verbatim;
	@Singular
	private List<String> loads;
	@Singular
	private List<Operation> operations;

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		if (withSchema) {
			args.add(WITHSCHEMA);
		}
		if (verbatim) {
			args.add(VERBATIM);
		}
		if (!loads.isEmpty()) {
			args.add(LOAD);
			args.add(loads.size());
			loads.forEach(load -> args.addProperty(load));
		}
		operations.forEach(operation -> operation.build(args));
	}

}
