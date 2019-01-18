package com.redislabs.lettusearch.search;

import java.util.ArrayList;
import java.util.List;

public class SearchResults<K, V> {

	private long count;
	private List<SearchResult<K, V>> results = new ArrayList<>();

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public List<SearchResult<K, V>> getResults() {
		return results;
	}

	public void setResults(List<SearchResult<K, V>> results) {
		this.results = results;
	}

}
