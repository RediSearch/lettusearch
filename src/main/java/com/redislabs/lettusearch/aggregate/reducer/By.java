package com.redislabs.lettusearch.aggregate.reducer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Builder
public @Getter @Setter class By {

	private String property;
	private Order order;

}