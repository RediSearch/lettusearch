package com.redislabs.lettusearch;

import io.lettuce.core.protocol.LettuceCharsets;
import io.lettuce.core.protocol.ProtocolKeyword;

public enum CommandKeyword implements ProtocolKeyword {

	ADD, MAXTEXTFIELDS, NOOFFSETS, NOHL, NOFIELDS, NOFREQS, STOPWORDS, SCHEMA, TEXT, WEIGHT, NUMERIC, GEO, PHONETIC, TAG, SEPARATOR, SORTABLE, NOSTEM,
	NOINDEX, NOSAVE, REPLACE, PARTIAL, LANGUAGE, PAYLOAD, IF, FIELDS, NOCONTENT, VERBATIM, NOSTOPWORDS, FUZZY, WITHPAYLOADS, WITHSORTKEYS, WITHSCORES, MAX, LIMIT, SORTBY, ASC, DESC, INCR, KEEPDOCS;

	public final byte[] bytes;

	private CommandKeyword() {
		bytes = name().getBytes(LettuceCharsets.ASCII);
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}
}
