package com.redislabs.lettusearch.aggregate;

import static com.redislabs.lettusearch.CommandKeyword.ASC;
import static com.redislabs.lettusearch.CommandKeyword.DESC;

import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class SortProperty extends PropertyArgument {

	private String property;
	@Default
	private Order order = Order.Asc;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		args.add(prefix(property));
		args.add(order == Order.Asc ? ASC : DESC);
	}

}
