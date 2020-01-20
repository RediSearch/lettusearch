package com.redislabs.lettusearch.aggregate.reducer;

import lombok.Builder;
import lombok.Data;

@Builder
public @Data class By {

	private String property;
	private Order order;

}