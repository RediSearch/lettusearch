package com.redislabs.lettusearch.aggregate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AggregateWithCursorResults<K, V> extends AggregateResults<K, V> {

	private static final long serialVersionUID = 4898688115426436989L;

	private long cursor;

}