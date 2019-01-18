package com.redislabs.lettusearch.search;

import static com.redislabs.lettusearch.CommandKeyword.IF;
import static com.redislabs.lettusearch.CommandKeyword.LANGUAGE;
import static com.redislabs.lettusearch.CommandKeyword.NOSAVE;
import static com.redislabs.lettusearch.CommandKeyword.PARTIAL;
import static com.redislabs.lettusearch.CommandKeyword.PAYLOAD;
import static com.redislabs.lettusearch.CommandKeyword.REPLACE;

import io.lettuce.core.CompositeArgument;
import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddOptions implements CompositeArgument {

	private boolean noSave;
	private boolean replace;
	private boolean replacePartial;
	private String language;
	private String payload;
	private String ifCondition;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
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
	}
}
