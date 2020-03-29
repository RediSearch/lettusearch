package com.redislabs.lettusearch.index.field;

public enum PhoneticMatcher {

	English("dm:en"), French("dm:fr"), Portuguese("dm:pt"), Spanish("dm:es");

	private String code;

	PhoneticMatcher(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}