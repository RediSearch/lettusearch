package com.redislabs.lettusearch;

import io.lettuce.core.protocol.LettuceCharsets;
import io.lettuce.core.protocol.ProtocolKeyword;

public enum CommandKeyword implements ProtocolKeyword {

	SCHEMA, TEXT, WEIGHT, NUMERIC, GEO, SORTABLE, NOSTEM, NOINDEX;

	public final byte[] bytes;

	private CommandKeyword() {
		bytes = name().getBytes(LettuceCharsets.ASCII);
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}
}
