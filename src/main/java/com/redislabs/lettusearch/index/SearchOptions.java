package com.redislabs.lettusearch.index;

import static com.redislabs.lettusearch.index.CommandKeyword.NOSTOPWORDS;
import static com.redislabs.lettusearch.index.CommandKeyword.VERBATIM;

import io.lettuce.core.CompositeArgument;
import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchOptions implements CompositeArgument {

	boolean verbatim;
	boolean noStopWords;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		if (verbatim) {
			args.add(VERBATIM);
		}
		if (noStopWords) {
			args.add(NOSTOPWORDS);
		}
	}

}
