package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.CommandKeyword.LOAD;
import static com.redislabs.lettusearch.CommandKeyword.VERBATIM;
import static com.redislabs.lettusearch.CommandKeyword.WITHSCHEMA;

import java.util.List;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
public class AggregateOptions implements RediSearchArgument {

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
