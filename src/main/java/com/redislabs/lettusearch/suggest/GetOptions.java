package com.redislabs.lettusearch.suggest;

import static com.redislabs.lettusearch.CommandKeyword.*;

import io.lettuce.core.CompositeArgument;
import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetOptions implements CompositeArgument {

	private boolean fuzzy;
	private boolean withScores;
	private boolean withPayloads;
	private Long max;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		if (fuzzy) {
			args.add(FUZZY);
		}
		if (withScores) {
			args.add(WITHSCORES);
		}
		if (withPayloads) {
			args.add(WITHPAYLOADS);
		}
		if (max != null) {
			args.add(MAX);
			args.add(max);
		}
	}

}
