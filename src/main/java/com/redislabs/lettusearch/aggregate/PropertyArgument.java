package com.redislabs.lettusearch.aggregate;

import io.lettuce.core.CompositeArgument;

public abstract class PropertyArgument implements CompositeArgument {

	private static final String PREFIX = "@";

	protected String prefix(String property) {
		return PREFIX + property;
	}

}
