package com.redislabs.lettusearch.search;

import static com.redislabs.lettusearch.CommandKeyword.ASC;
import static com.redislabs.lettusearch.CommandKeyword.DESC;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder
public class SortBy implements RediSearchArgument {

	public final static Direction DEFAULT_DIRECTION = Direction.Ascending;

	private String field;
	@Default
	private Direction direction = DEFAULT_DIRECTION;

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		args.add(field);
		args.add(direction == Direction.Ascending ? ASC : DESC);
	}

}
