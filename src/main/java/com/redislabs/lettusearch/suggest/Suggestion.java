package com.redislabs.lettusearch.suggest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Suggestion<V> {

	private V string;
	private Double score;
	private V payload;

}
