package com.redislabs.lettusearch.search;

import static com.redislabs.lettusearch.CommandKeyword.ASC;
import static com.redislabs.lettusearch.CommandKeyword.DESC;

import io.lettuce.core.CompositeArgument;
import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder
public class SortBy implements CompositeArgument {

	public enum Direction {
		Ascending, Descending
	}

	private String field;
	@Default
	private Direction direction = Direction.Ascending;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		args.add(field);
		args.add(direction == Direction.Ascending ? ASC : DESC);
	}

}
