package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.CommandKeyword.ASC;
import static com.redislabs.lettusearch.CommandKeyword.DESC;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder
public class SortProperty implements RediSearchArgument {

	private String property;
	@Default
	private Order order = Order.Asc;

	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		args.addProperty(property);
		args.add(order == Order.Asc ? ASC : DESC);
	}

}
