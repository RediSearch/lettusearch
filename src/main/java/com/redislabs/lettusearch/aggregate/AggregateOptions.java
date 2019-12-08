package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.CommandKeyword.LOAD;
import static com.redislabs.lettusearch.CommandKeyword.VERBATIM;
import static com.redislabs.lettusearch.CommandKeyword.WITHSCHEMA;

import java.util.ArrayList;
import java.util.List;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public @Data class AggregateOptions implements RediSearchArgument {

	private boolean withSchema;
	private boolean verbatim;
	private List<String> loads = new ArrayList<>();
	private List<Operation> operations = new ArrayList<>();

	public AggregateOptions load(String load) {
		loads.add(load);
		return this;
	}

	public AggregateOptions operation(Operation operation) {
		operations.add(operation);
		return this;
	}

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
