package com.redislabs.lettusearch.aggregate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
public @Data class AggregateWithCursorResults<K, V> extends AggregateResults<K, V> {

	private static final long serialVersionUID = 4898688115426436989L;

	private Long cursor;

}