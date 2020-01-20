package com.redislabs.lettusearch.search;

import lombok.Builder;
import lombok.Data;

@Builder
public @Data class TagOptions {

	private String open;
	private String close;

}