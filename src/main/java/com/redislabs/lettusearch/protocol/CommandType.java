package com.redislabs.lettusearch.protocol;

import java.nio.charset.StandardCharsets;

import io.lettuce.core.protocol.ProtocolKeyword;

/**
 * RediSearch commands.
 *
 * @author Julien Ruaux
 */
public enum CommandType implements ProtocolKeyword {

	ADD, AGGREGATE, ALTER, CREATE, CURSOR, DEL, DROP, GET, MGET, INFO, SEARCH, SUGADD, SUGGET, SUGDEL, SUGLEN, ALIASADD,
	ALIASUPDATE, ALIASDEL, _LIST;

	private final static String PREFIX = "FT.";

	public final byte[] bytes;

	CommandType() {
		bytes = (PREFIX + name()).getBytes(StandardCharsets.US_ASCII);
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}
}
