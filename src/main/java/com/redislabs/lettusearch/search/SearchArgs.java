package com.redislabs.lettusearch.search;

import lombok.Builder;
import lombok.Data;

@Builder
public @Data class SearchArgs {

	private String query;
	private SearchOptions options;

}
