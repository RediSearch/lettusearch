package com.redislabs.lettusearch.api;

import static com.redislabs.lettusearch.api.CommandKeyword.FIELDS;
import static com.redislabs.lettusearch.api.CommandKeyword.IF;
import static com.redislabs.lettusearch.api.CommandKeyword.LANGUAGE;
import static com.redislabs.lettusearch.api.CommandKeyword.NOSAVE;
import static com.redislabs.lettusearch.api.CommandKeyword.PARTIAL;
import static com.redislabs.lettusearch.api.CommandKeyword.PAYLOAD;
import static com.redislabs.lettusearch.api.CommandKeyword.REPLACE;

import java.util.Map;

import io.lettuce.core.CompositeArgument;
import io.lettuce.core.internal.LettuceAssert;
import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Document implements CompositeArgument {

	static final String MUST_NOT_BE_NULL = "must not be null";
	static final String MUST_NOT_BE_EMPTY = "must not be empty";

	private String id;
	@Builder.Default
	private double score = 1;
	private boolean noSave;
	private boolean replace;
	private boolean replacePartial;
	private String language;
	private String payload;
	private String ifCondition;
	private Map<String, String> fields;

	@SuppressWarnings("unchecked")
	public <K, V> void build(CommandArgs<K, V> args) {
		LettuceAssert.notNull(id, "id " + MUST_NOT_BE_NULL);
		LettuceAssert.notNull(score, "score " + MUST_NOT_BE_NULL);
		args.add(id);
		args.add(score);
		if (noSave) {
			args.add(NOSAVE);
		}
		if (replace) {
			args.add(REPLACE);
			if (replacePartial) {
				args.add(PARTIAL);
			}
		}
		if (language != null) {
			args.add(LANGUAGE);
			args.add(language);
		}
		if (payload != null) {
			args.add(PAYLOAD);
			args.add(payload);
		}
		if (ifCondition != null) {
			args.add(IF);
			args.add(ifCondition);
		}
		LettuceAssert.isTrue(!fields.isEmpty(), "fields " + MUST_NOT_BE_EMPTY);
		args.add(FIELDS);
		args.add((Map<K, V>) fields);
	}
}
