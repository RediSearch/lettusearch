package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.protocol.CommandKeyword.COUNT;
import static com.redislabs.lettusearch.protocol.CommandKeyword.MAXIDLE;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;

@Builder
public @Data class CursorOptions implements RediSearchArgument {

	private Long count;
	private Long maxIdle;

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		if (count != null) {
			args.add(COUNT);
			args.add(count);
		}
		if (maxIdle != null) {
			args.add(MAXIDLE);
			args.add(maxIdle);
		}
	}

}
