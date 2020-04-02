package com.redislabs.lettusearch.suggest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public @Data class Suggestion<V> {

	private V string;
	private Double score;
	private V payload;

}
