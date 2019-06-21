package com.redislabs.lettusearch.aggregate;

import lombok.Getter;
import lombok.Setter;

public class AggregateWithCursorResults<K, V> extends AggregateResults<K, V> {

	private static final long serialVersionUID = 4898688115426436989L;

	@Getter
	@Setter
	private Long cursor;

}