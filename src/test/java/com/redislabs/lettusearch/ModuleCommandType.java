package com.redislabs.lettusearch;

import io.lettuce.core.protocol.LettuceCharsets;
import io.lettuce.core.protocol.ProtocolKeyword;

public enum ModuleCommandType implements ProtocolKeyword {

	MODULE;

	private final static String PREFIX = "";

	public final byte[] bytes;

	ModuleCommandType() {
		bytes = (PREFIX + name()).getBytes(LettuceCharsets.ASCII);
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}
}