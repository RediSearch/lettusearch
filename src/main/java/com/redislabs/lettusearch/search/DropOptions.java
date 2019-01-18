package com.redislabs.lettusearch.search;

import static com.redislabs.lettusearch.CommandKeyword.KEEPDOCS;

import io.lettuce.core.CompositeArgument;
import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DropOptions implements CompositeArgument {

	private boolean keepDocs;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		if (keepDocs) {
			args.add(KEEPDOCS);
		}
	}

}
