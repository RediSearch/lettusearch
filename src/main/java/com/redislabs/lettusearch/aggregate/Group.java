package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.CommandKeyword.GROUPBY;

import java.util.List;

import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
public class Group implements Operation {

	@Singular
	private List<String> properties;
	@Singular
	private List<Reducer> reduces;

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		args.add(GROUPBY);
		args.add(properties.size());
		properties.forEach(property -> args.addProperty(property));
		reduces.forEach(reduce -> reduce.build(args));
	}

}
