package com.redislabs.lettusearch.index;

import static com.redislabs.lettusearch.index.CommandKeyword.FIELDS;
import static com.redislabs.lettusearch.index.CommandKeyword.IF;
import static com.redislabs.lettusearch.index.CommandKeyword.LANGUAGE;
import static com.redislabs.lettusearch.index.CommandKeyword.NOSAVE;
import static com.redislabs.lettusearch.index.CommandKeyword.PARTIAL;
import static com.redislabs.lettusearch.index.CommandKeyword.PAYLOAD;
import static com.redislabs.lettusearch.index.CommandKeyword.REPLACE;

import java.util.Map;

import io.lettuce.core.CompositeArgument;
import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Document implements CompositeArgument {

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
		args.add(FIELDS);
		args.add((Map<K, V>) fields);
	}
}
