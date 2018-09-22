package com.redislabs.lettusearch.suggest;

import static com.redislabs.lettusearch.index.CommandKeyword.FUZZY;
import static com.redislabs.lettusearch.index.CommandKeyword.MAX;

import io.lettuce.core.CompositeArgument;
import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuggestionOptions implements CompositeArgument {

	private boolean fuzzy;
	private Integer maxResults;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		if (fuzzy) {
			args.add(FUZZY);
		}
		if (maxResults != null) {
			args.add(MAX);
			args.add(maxResults);
		}
	}

}
