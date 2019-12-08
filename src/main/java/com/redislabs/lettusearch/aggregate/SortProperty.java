package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.CommandKeyword.ASC;
import static com.redislabs.lettusearch.CommandKeyword.DESC;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public @Data class SortProperty implements RediSearchArgument {

	public final static Order DEFAULT_ORDER = Order.Asc;

	private String property;
	private Order order = DEFAULT_ORDER;

	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		args.addProperty(property);
		args.add(order == Order.Asc ? ASC : DESC);
	}

}
