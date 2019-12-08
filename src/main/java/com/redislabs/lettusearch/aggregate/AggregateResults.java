package com.redislabs.lettusearch.aggregate;

import java.util.ArrayList;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
public @Data class AggregateResults<K, V> extends ArrayList<Map<K, V>> {

	private static final long serialVersionUID = 2860244139719400188L;

	private long count;

}