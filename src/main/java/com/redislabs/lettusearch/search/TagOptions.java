package com.redislabs.lettusearch.search;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Builder
public @Data class TagOptions {

	private String open;
	private String close;

}