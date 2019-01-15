package com.redislabs.lettusearch.api;

import io.lettuce.core.protocol.LettuceCharsets;
import io.lettuce.core.protocol.ProtocolKeyword;

public enum CommandKeyword implements ProtocolKeyword {

	ADD, MAXTEXTFIELDS, NOOFFSETS, NOHL, NOFIELDS, NOFREQS, STOPWORDS, SCHEMA, TEXT, WEIGHT, NUMERIC, GEO, SORTABLE, NOSTEM,
	NOINDEX, NOSAVE, REPLACE, PARTIAL, LANGUAGE, PAYLOAD, IF, FIELDS, VERBATIM, NOSTOPWORDS, FUZZY, WITHPAYLOADS, WITHSCORES, MAX, LIMIT, SORTBY, ASC, DESC, INCR, KEEPDOCS;

	public final byte[] bytes;

	private CommandKeyword() {
		bytes = name().getBytes(LettuceCharsets.ASCII);
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}
}
