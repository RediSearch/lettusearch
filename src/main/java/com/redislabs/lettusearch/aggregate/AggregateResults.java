package com.redislabs.lettusearch.aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AggregateResults<K, V> {

	private long count;
	private List<Map<K, V>> results = new ArrayList<>();

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public List<Map<K, V>> getResults() {
		return results;
	}

	public void setResults(List<Map<K, V>> results) {
		this.results = results;
	}

}