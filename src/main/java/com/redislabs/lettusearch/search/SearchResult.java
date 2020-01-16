package com.redislabs.lettusearch.search;

import java.util.LinkedHashMap;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public @Data class SearchResult<K, V> extends LinkedHashMap<K, V> {

	private static final long serialVersionUID = -8178671346134635131L;

	private K documentId;
	private Double score;

}
