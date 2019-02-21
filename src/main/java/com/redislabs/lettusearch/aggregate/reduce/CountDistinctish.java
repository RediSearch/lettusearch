package com.redislabs.lettusearch.aggregate.reduce;

import static com.redislabs.lettusearch.CommandKeyword.COUNT_DISTINCTISH;

import com.redislabs.lettusearch.aggregate.Function;
import com.redislabs.lettusearch.aggregate.PropertyArgument;

import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class CountDistinctish extends PropertyArgument implements Function {

	private String property;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		args.add(COUNT_DISTINCTISH);
		args.add(1);
		args.add(prefix(property));
	}

}
