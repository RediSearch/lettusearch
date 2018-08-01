package com.redislabs.lettusearch.index;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class AbstractSearchResults<K, V> {

	private long count;
	private List<SearchResult<K, V>> results = new ArrayList<>();

}
