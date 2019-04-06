package com.redislabs.lettusearch.search.field;

import static com.redislabs.lettusearch.CommandKeyword.GEO;
import static com.redislabs.lettusearch.CommandKeyword.NUMERIC;
import static com.redislabs.lettusearch.CommandKeyword.TAG;
import static com.redislabs.lettusearch.CommandKeyword.TEXT;

import com.redislabs.lettusearch.CommandKeyword;

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
