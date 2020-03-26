package com.redislabs.lettusearch.suggest;

import lombok.Builder;
import lombok.Data;

@Builder
public @Data class SugaddArgs<V> {

	private V string;
	private double score;
	private boolean increment;
	private V payload;

}
