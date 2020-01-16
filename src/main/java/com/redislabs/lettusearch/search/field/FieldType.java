package com.redislabs.lettusearch.search.field;

import static com.redislabs.lettusearch.protocol.CommandKeyword.GEO;
import static com.redislabs.lettusearch.protocol.CommandKeyword.NUMERIC;
import static com.redislabs.lettusearch.protocol.CommandKeyword.TAG;
import static com.redislabs.lettusearch.protocol.CommandKeyword.TEXT;

import com.redislabs.lettusearch.protocol.CommandKeyword;

public enum FieldType {

	Text(TEXT), Numeric(NUMERIC), Geo(GEO), Tag(TAG);

	private CommandKeyword keyword;

	FieldType(CommandKeyword keyword) {
		this.keyword = keyword;
	}

	public CommandKeyword getKeyword() {
		return keyword;
	}
}
