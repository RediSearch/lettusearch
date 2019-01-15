package com.redislabs.lettusearch.api;

import static com.redislabs.lettusearch.api.CommandKeyword.LIMIT;
import static com.redislabs.lettusearch.api.CommandKeyword.NOSTOPWORDS;
import static com.redislabs.lettusearch.api.CommandKeyword.SORTBY;
import static com.redislabs.lettusearch.api.CommandKeyword.VERBATIM;

import io.lettuce.core.CompositeArgument;
import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchOptions implements CompositeArgument {

	private boolean verbatim;
	private boolean noStopWords;
	private SortBy sortBy;
	private Limit limit;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		if (verbatim) {
			args.add(VERBATIM);
		}
		if (noStopWords) {
			args.add(NOSTOPWORDS);
		}
		if (sortBy != null) {
			args.add(SORTBY);
			sortBy.build(args);
		}
		if (limit != null) {
			args.add(LIMIT);
			limit.build(args);
		}
	}

}
