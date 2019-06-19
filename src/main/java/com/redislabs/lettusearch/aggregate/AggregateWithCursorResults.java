package com.redislabs.lettusearch.aggregate;

import lombok.Data;

@Data
public class AggregateWithCursorResults<K, V> {

	private AggregateResults<K, V> results = new AggregateResults<>();
	private Long cursor;

}