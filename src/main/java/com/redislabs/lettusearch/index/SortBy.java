package com.redislabs.lettusearch.index;

import static com.redislabs.lettusearch.index.CommandKeyword.ASC;
import static com.redislabs.lettusearch.index.CommandKeyword.DESC;

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
