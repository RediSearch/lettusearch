package com.redislabs.lettusearch.suggest;

import lombok.Data;

@Data
public class SuggetResult<V> {

	private V string;
	private Double score;
	private V payload;

}
