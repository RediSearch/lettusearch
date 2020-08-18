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

	public static <V> SuggestionBuilder<V> builder() {
		return new SuggestionBuilder<>();
	}

	@Setter
	@Accessors(fluent = true)
	public static class SuggestionBuilder<V> {

		private V string;
		private double score = 1;
		private V payload;

		public Suggestion<V> build() {
			LettuceAssert.notNull(string, "String is required.");
			Suggestion<V> suggestion = new Suggestion<>();
			suggestion.setString(string);
			suggestion.setScore(score);
			suggestion.setPayload(payload);
			return suggestion;
		}

	}

}
