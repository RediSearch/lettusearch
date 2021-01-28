package com.redislabs.lettusearch.index.field;

import com.redislabs.lettusearch.protocol.CommandKeyword;

public enum FieldType {

	TEXT(CommandKeyword.TEXT), NUMERIC(CommandKeyword.NUMERIC), GEO(CommandKeyword.GEO), TAG(CommandKeyword.TAG);

	private final CommandKeyword keyword;

	FieldType(CommandKeyword keyword) {
		this.keyword = keyword;
	}

	public CommandKeyword getKeyword() {
		return keyword;
	}
}
