package com.redislabs.lettusearch.aggregate;

import java.util.ArrayList;
import java.util.Map;

public class AggregateResults<K, V> extends ArrayList<Map<K, V>> {

	private static final long serialVersionUID = 2860244139719400188L;

	private long count;

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

}