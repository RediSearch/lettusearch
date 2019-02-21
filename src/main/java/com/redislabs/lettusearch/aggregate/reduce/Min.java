package com.redislabs.lettusearch.aggregate.reduce;

import static com.redislabs.lettusearch.CommandKeyword.MIN;

import com.redislabs.lettusearch.aggregate.Function;
import com.redislabs.lettusearch.aggregate.PropertyArgument;

import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class Min extends PropertyArgument implements Function {

	private String property;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		args.add(MIN);
		args.add(1);
		args.add(prefix(property));
	}

}
