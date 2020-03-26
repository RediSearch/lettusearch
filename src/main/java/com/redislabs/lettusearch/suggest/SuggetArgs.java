package com.redislabs.lettusearch.suggest;

import lombok.Builder;
import lombok.Data;

@Builder
public @Data class SuggetArgs<V> {

	private V prefix;
	private SuggetOptions options;

}
