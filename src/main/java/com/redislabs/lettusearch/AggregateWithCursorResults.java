package com.redislabs.lettusearch;

public class AggregateWithCursorResults<K> extends AggregateResults<K> {

	private long cursor;

	public long getCursor() {
		return cursor;
	}

	public void setCursor(long cursor) {
		this.cursor = cursor;
	}
}