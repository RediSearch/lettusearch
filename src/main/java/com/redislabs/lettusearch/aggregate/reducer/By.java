package com.redislabs.lettusearch.aggregate.reducer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class By {

	private String property;
	private Order order;

	public static enum Order {
		Asc, Desc
	}
}