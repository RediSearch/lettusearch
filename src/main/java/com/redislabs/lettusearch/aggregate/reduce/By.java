package com.redislabs.lettusearch.aggregate.reduce;

import static com.redislabs.lettusearch.CommandKeyword.*;

import com.redislabs.lettusearch.aggregate.Order;
import com.redislabs.lettusearch.aggregate.PropertyArgument;

import io.lettuce.core.protocol.CommandArgs;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class By extends PropertyArgument {

	private String property;
	private Order order;

	@Override
	public <K, V> void build(CommandArgs<K, V> args) {
		args.add(BY);
		args.add(prefix(property));
		if (order != null) {
			args.add(order == Order.Asc ? ASC : DESC);
		}
	}

	public int getNumberOfArgs() {
		return order == null ? 2 : 3;
	}

}
