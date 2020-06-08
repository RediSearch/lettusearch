package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.protocol.CommandKeyword.GROUPBY;

import java.util.List;

import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
public class Group implements Operation {

	@Singular
	private List<String> properties;
	@Singular
	private List<Reducer> reducers;

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		args.add(GROUPBY);
		args.add(properties.size());
		properties.forEach(args::addProperty);
		reducers.forEach(reducer -> reducer.build(args));
	}

}
