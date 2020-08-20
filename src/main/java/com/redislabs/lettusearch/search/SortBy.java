package com.redislabs.lettusearch.search;

import static com.redislabs.lettusearch.protocol.CommandKeyword.ASC;
import static com.redislabs.lettusearch.protocol.CommandKeyword.DESC;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder
public class SortBy<K> implements RediSearchArgument {

	public final static Direction DEFAULT_DIRECTION = Direction.Ascending;

	private K field;
	@Default
	private Direction direction = DEFAULT_DIRECTION;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void build(RediSearchCommandArgs args) {
		args.addKey(field);
		args.add(direction == Direction.Ascending ? ASC : DESC);
	}

}
