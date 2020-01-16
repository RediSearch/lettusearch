package com.redislabs.lettusearch.aggregate.reducer;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Builder
public @Data class By {

	private String property;
	private Order order;

}