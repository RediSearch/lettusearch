package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.protocol.CommandKeyword.ASC;
import static com.redislabs.lettusearch.protocol.CommandKeyword.DESC;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SortProperty implements RediSearchArgument {

	public final static Order DEFAULT_ORDER = Order.Asc;

	private String property;
	@Builder.Default
	private Order order = DEFAULT_ORDER;

	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		args.addProperty(property);
		args.add(order == Order.Asc ? ASC : DESC);
	}

}
