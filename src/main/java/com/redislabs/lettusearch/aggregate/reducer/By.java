package com.redislabs.lettusearch.aggregate.reducer;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public @Data class By {

	private String property;
	private Order order;

}