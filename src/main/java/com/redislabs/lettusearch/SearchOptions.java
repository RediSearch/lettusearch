package com.redislabs.lettusearch;

import java.util.List;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.impl.protocol.RediSearchCommandArgs;

import lombok.*;

import static com.redislabs.lettusearch.impl.protocol.CommandKeyword.*;

@Data
@Builder
public class SearchOptions<K> implements RediSearchArgument {

	private boolean noContent;
	private boolean verbatim;
	private boolean noStopWords;
	private boolean withScores;
	private boolean withPayloads;
	private boolean withSortKeys;
	@Singular
	private List<K> inKeys;
	@Singular
	private List<K> inFields;
	@Singular
	private List<K> returnFields;
	private Highlight<K> highlight;
	private Language language;
	private SortBy<K> sortBy;
	private Limit limit;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void build(RediSearchCommandArgs args) {
		if (noContent) {
			args.add(NOCONTENT);
		}
		if (verbatim) {
			args.add(VERBATIM);
		}
		if (noStopWords) {
			args.add(NOSTOPWORDS);
		}
		if (withScores) {
			args.add(WITHSCORES);
		}
		if (withPayloads) {
			args.add(WITHPAYLOADS);
		}
		if (withSortKeys) {
			args.add(WITHSORTKEYS);
		}
		if (!inKeys.isEmpty()) {
			args.add(INKEYS);
			args.add(inKeys.size());
			inKeys.forEach(args::addKey);
		}
		if (!inFields.isEmpty()) {
			args.add(INFIELDS);
			args.add(inFields.size());
			inFields.forEach(args::addKey);
		}
		if (!returnFields.isEmpty()) {
			args.add(RETURN);
			args.add(returnFields.size());
			returnFields.forEach(args::addKey);
		}
		if (highlight != null) {
			args.add(HIGHLIGHT);
			highlight.build(args);
		}
		if (sortBy != null) {
			args.add(SORTBY);
			sortBy.build(args);
		}
		if (language != null) {
			args.add(LANGUAGE);
			args.add(language.name());
		}
		if (limit != null) {
			limit.build(args);
		}
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Highlight<K> implements RediSearchArgument {

		@Singular
		private List<K> fields;
		private Tag<K> tag;

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public void build(RediSearchCommandArgs args) {
			if (fields.size() > 0) {
				args.add(FIELDS);
				args.add(fields.size());
				fields.forEach(args::addKey);
			}
			if (tag != null) {
				args.add(TAGS);
				args.addKey(tag.getOpen());
				args.addKey(tag.getClose());
			}
		}

		@Data
		@Builder
		public static class Tag<K> {

			private K open;
			private K close;

		}
	}

	@Data
	@Builder
	public static class Limit implements RediSearchArgument {

		public final static long DEFAULT_OFFSET = 0;
		public final static long DEFAULT_NUM = 10;

		@Builder.Default
		private long offset = DEFAULT_OFFSET;
		@Builder.Default
		private long num = DEFAULT_NUM;

		@Override
		public <K, V> void build(RediSearchCommandArgs<K, V> args) {
			args.add(LIMIT);
			args.add(offset);
			args.add(num);
		}

	}

	public enum Language {
		Arabic, Danish, Dutch, English, Finnish, French, German, Hungarian, Italian, Norwegian, Portuguese, Romanian,
		Russian, Spanish, Swedish, Tamil, Turkish, Chinese
	}

	@Data
	@Builder
	public static class SortBy<K> implements RediSearchArgument {

		public final static Direction DEFAULT_DIRECTION = Direction.Ascending;

		private K field;
		@Builder.Default
		private Direction direction = DEFAULT_DIRECTION;

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public void build(RediSearchCommandArgs args) {
			args.addKey(field);
			args.add(direction == Direction.Ascending ? ASC : DESC);
		}

		public enum Direction {
			Ascending, Descending
		}
	}
}
