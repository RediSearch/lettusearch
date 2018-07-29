package com.redislabs.lettusearch.index;

import io.lettuce.core.protocol.LettuceCharsets;
import io.lettuce.core.protocol.ProtocolKeyword;

public enum CommandKeyword implements ProtocolKeyword {

	MAXTEXTFIELDS, NOOFFSETS, NOHL, NOFIELDS, NOFREQS, STOPWORDS, SCHEMA, TEXT, WEIGHT, NUMERIC, GEO, SORTABLE, NOSTEM,
	NOINDEX, NOSAVE, REPLACE, PARTIAL, LANGUAGE, PAYLOAD, IF, FIELDS;

	public final byte[] bytes;

	private CommandKeyword() {
		bytes = name().getBytes(LettuceCharsets.ASCII);
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}
}
