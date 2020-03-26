package com.redislabs.lettusearch.suggest;

import static com.redislabs.lettusearch.protocol.CommandKeyword.FUZZY;
import static com.redislabs.lettusearch.protocol.CommandKeyword.MAX;
import static com.redislabs.lettusearch.protocol.CommandKeyword.WITHPAYLOADS;
import static com.redislabs.lettusearch.protocol.CommandKeyword.WITHSCORES;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;

@Builder
public @Data class SuggetOptions implements RediSearchArgument {

	private boolean fuzzy;
	private boolean withScores;
	private boolean withPayloads;
	private Long max;

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
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
