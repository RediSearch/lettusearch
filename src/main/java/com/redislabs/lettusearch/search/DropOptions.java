package com.redislabs.lettusearch.search;

import static com.redislabs.lettusearch.protocol.CommandKeyword.KEEPDOCS;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Builder
public @Data class DropOptions implements RediSearchArgument {

	private boolean keepDocs;

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		if (keepDocs) {
			args.add(KEEPDOCS);
		}
	}

}
