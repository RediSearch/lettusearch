package com.redislabs.lettusearch.suggest;

import static com.redislabs.lettusearch.CommandKeyword.FUZZY;
import static com.redislabs.lettusearch.CommandKeyword.MAX;
import static com.redislabs.lettusearch.CommandKeyword.WITHPAYLOADS;
import static com.redislabs.lettusearch.CommandKeyword.WITHSCORES;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuggestGetOptions implements RediSearchArgument {

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
