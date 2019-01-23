package com.redislabs.lettusearch.search.field;

public enum Matcher {

	English("dm:en"), French("dm:fr"), Portuguese("dm:pt"), Spanish("dm:es");

	private String code;

	Matcher(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
