package com.redislabs.lettusearch.index;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Data;

@Data
public class SearchResult<K, V> {

	private K documentId;
	private Double score;
	private Map<K, V> fields = new LinkedHashMap<>();

}
