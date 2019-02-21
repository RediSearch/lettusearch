package com.redislabs.lettusearch.aggregate.reduce;

import static com.redislabs.lettusearch.CommandKeyword.*;

import com.redislabs.lettusearch.aggregate.Function;

import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Count implements Function {

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		args.add(COUNT);
		args.add(0);
	}

}
