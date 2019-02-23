package com.redislabs.lettusearch.search;

import static com.redislabs.lettusearch.CommandKeyword.KEEPDOCS;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DropOptions implements RediSearchArgument {

	private boolean keepDocs;

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		if (keepDocs) {
			args.add(KEEPDOCS);
		}
	}

}
