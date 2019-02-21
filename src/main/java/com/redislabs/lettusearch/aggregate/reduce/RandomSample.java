package com.redislabs.lettusearch.aggregate.reduce;

import static com.redislabs.lettusearch.CommandKeyword.RANDOM_SAMPLE;

import com.redislabs.lettusearch.aggregate.Function;
import com.redislabs.lettusearch.aggregate.PropertyArgument;

import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class RandomSample extends PropertyArgument implements Function {

	private String property;
	private int size;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		args.add(RANDOM_SAMPLE);
		args.add(2);
		args.add(prefix(property));
		args.add(size);
	}

}
