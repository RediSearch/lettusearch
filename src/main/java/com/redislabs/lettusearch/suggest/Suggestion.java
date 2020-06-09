package com.redislabs.lettusearch.suggest;

import io.lettuce.core.internal.LettuceAssert;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

public class Suggestion<V> {

	@Getter
	@Setter
	private V string;
	@Getter
	@Setter
	private Double score;
	@Getter
	@Setter
	private V payload;

	public static SuggestionBuilder builder() {
		return new SuggestionBuilder();
	}

	@Setter
	@Accessors(fluent = true)
	public static class SuggestionBuilder {

		private String string;
		private double score = 1;
		private String payload;

		public Suggestion<String> build() {
			LettuceAssert.notNull(string, "String is required.");
			Suggestion<String> suggestion = new Suggestion<>();
			suggestion.setString(string);
			suggestion.setScore(score);
			suggestion.setPayload(payload);
			return suggestion;
		}

	}

}
