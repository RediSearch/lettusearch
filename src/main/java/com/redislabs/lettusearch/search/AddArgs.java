package com.redislabs.lettusearch.search;

import lombok.Builder;
import lombok.Data;

@Builder
public @Data class AddArgs<K, V> {

	private Document<K, V> document;
	private V payload;
	private AddOptions options;

}
