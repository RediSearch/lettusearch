package com.redislabs.lettusearch.aggregate.reduce;

import static com.redislabs.lettusearch.CommandKeyword.QUANTILE;

import com.redislabs.lettusearch.aggregate.Function;
import com.redislabs.lettusearch.aggregate.PropertyArgument;

import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class Quantile extends PropertyArgument implements Function {

	private String property;
	private double quantile;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		args.add(QUANTILE);
		args.add(2);
		args.add(prefix(property));
		args.add(quantile);
	}

}
