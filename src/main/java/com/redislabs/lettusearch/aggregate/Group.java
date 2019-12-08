package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.CommandKeyword.GROUPBY;

import java.util.ArrayList;
import java.util.List;

import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public @Data class Group implements Operation {

	private List<String> properties = new ArrayList<>();
	private List<Reducer> reducers = new ArrayList<>();

	public Group property(String property) {
		properties.add(property);
		return this;
	}

	public Group reducer(Reducer reducer) {
		reducers.add(reducer);
		return this;
	}

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		args.add(GROUPBY);
		args.add(properties.size());
		properties.forEach(property -> args.addProperty(property));
		reducers.forEach(reducer -> reducer.build(args));
	}

}
