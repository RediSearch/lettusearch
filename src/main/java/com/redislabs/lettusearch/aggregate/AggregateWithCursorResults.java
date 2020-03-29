package com.redislabs.lettusearch.aggregate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public @Data class AggregateWithCursorResults<K, V> extends AggregateResults<K, V> {

	private static final long serialVersionUID = 4898688115426436989L;

	private long cursor;

}