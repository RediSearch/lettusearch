package com.redislabs.lettusearch.search;

import java.util.LinkedHashMap;

import lombok.Getter;
import lombok.Setter;

public class SearchResult<K, V> extends LinkedHashMap<K, V> {

	private static final long serialVersionUID = -8178671346134635131L;

	@Getter
	@Setter
	private K documentId;
	@Getter
	@Setter
	private Double score;

}
