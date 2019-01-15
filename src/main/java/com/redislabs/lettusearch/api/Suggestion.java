package com.redislabs.lettusearch.api;

import static com.redislabs.lettusearch.api.CommandKeyword.INCR;
import static com.redislabs.lettusearch.api.CommandKeyword.PAYLOAD;

import io.lettuce.core.CompositeArgument;
import io.lettuce.core.internal.LettuceAssert;
import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Suggestion implements CompositeArgument {

	static final String MUST_NOT_BE_NULL = "must not be null";
	static final String MUST_NOT_BE_EMPTY = "must not be empty";

	private String string;
	@Builder.Default
	private Double score = 1d;
	private boolean increment;
	private String payload;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		LettuceAssert.notNull(string, "string " + MUST_NOT_BE_NULL);
		LettuceAssert.notNull(score, "score " + MUST_NOT_BE_NULL);
		args.add(string);
		args.add(score);
		if (increment) {
			args.add(INCR);
		}
		if (payload != null) {
			args.add(PAYLOAD);
			args.add(payload);
		}
	}
}
