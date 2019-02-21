package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.CommandKeyword.LOAD;
import static com.redislabs.lettusearch.CommandKeyword.VERBATIM;
import static com.redislabs.lettusearch.CommandKeyword.WITHSCHEMA;

import java.util.List;

import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Singular;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class AggregateOptions extends PropertyArgument {

	private boolean withSchema;
	private boolean verbatim;
	@Singular
	private List<String> loads;
	@Singular
	private List<Operation> operations;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		if (withSchema) {
			args.add(WITHSCHEMA);
		}
		if (verbatim) {
			args.add(VERBATIM);
		}
		if (!loads.isEmpty()) {
			args.add(LOAD);
			args.add(loads.size());
			loads.forEach(load -> args.add(prefix(load)));
		}
		operations.forEach(operation -> operation.build(args));
	}

}
