package com.redislabs.lettusearch.aggregate;

import lombok.Builder;
import lombok.Data;

@Builder
public @Data class AggregateArgs {

	private String query;
	private AggregateOptions options;

}
