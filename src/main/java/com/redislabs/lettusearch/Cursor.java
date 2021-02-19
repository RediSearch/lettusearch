package com.redislabs.lettusearch;

import static com.redislabs.lettusearch.impl.protocol.CommandKeyword.COUNT;
import static com.redislabs.lettusearch.impl.protocol.CommandKeyword.MAXIDLE;

import com.redislabs.lettusearch.impl.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Cursor {

	private Long count;
	private Long maxIdle;

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
