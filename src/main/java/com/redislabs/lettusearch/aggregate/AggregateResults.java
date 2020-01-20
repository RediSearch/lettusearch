package com.redislabs.lettusearch.aggregate;

import java.util.ArrayList;
import java.util.Map;

import lombok.Data;

public @Data class AggregateResults<K, V> extends ArrayList<Map<K, V>> {

	private static final long serialVersionUID = 2860244139719400188L;

	private long count;

}