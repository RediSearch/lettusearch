package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.CommandKeyword.MAX;
import static com.redislabs.lettusearch.CommandKeyword.SORTBY;

import java.util.ArrayList;
import java.util.List;

import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public @Data class Sort implements Operation {

	private List<SortProperty> properties = new ArrayList<>();
	private Long max;

	public Sort property(SortProperty property) {
		properties.add(property);
		return this;
	}

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		args.add(SORTBY);
		args.add(properties.size() * 2);
		properties.forEach(property -> property.build(args));
		if (max != null) {
			args.add(MAX);
			args.add(max);
		}

	}

}
