package com.redislabs.lettusearch;

import io.lettuce.core.protocol.LettuceCharsets;
import io.lettuce.core.protocol.ProtocolKeyword;

/**
 * RediSearch commands.
 *
 * @author Julien Ruaux
 */
public enum CommandType implements ProtocolKeyword {

	ADD, CREATE, DROP, GET, DEL, SEARCH, SUGADD, SUGGET, AGGREGATE;

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
