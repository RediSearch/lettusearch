package com.redislabs.lettusearch;

import io.lettuce.core.protocol.LettuceCharsets;
import io.lettuce.core.protocol.ProtocolKeyword;

/**
 * RediSearch commands.
 *
 * @author Julien Ruaux
 */
public enum CommandType implements ProtocolKeyword {

	ADD, AGGREGATE, ALTER, CREATE, CURSOR, DEL, DROP, GET, MGET, INFO, SEARCH, SUGADD, SUGGET, ALIASADD, ALIASUPDATE,
	ALIASDEL;

	private final static String PREFIX = "FT.";

	public final byte[] bytes;

	CommandType() {
		bytes = (PREFIX + name()).getBytes(LettuceCharsets.ASCII);
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}
}
