package com.redislabs.lettusearch;

import static com.redislabs.lettusearch.protocol.CommandKeyword.NOINDEX;
import static com.redislabs.lettusearch.protocol.CommandKeyword.NOSTEM;
import static com.redislabs.lettusearch.protocol.CommandKeyword.SORTABLE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redislabs.lettusearch.index.IndexInfo;
import com.redislabs.lettusearch.index.field.Field;
import com.redislabs.lettusearch.index.field.GeoField;
import com.redislabs.lettusearch.index.field.NumericField;
import com.redislabs.lettusearch.index.field.TagField;
import com.redislabs.lettusearch.index.field.TextField;
import com.redislabs.lettusearch.protocol.CommandKeyword;

public class RediSearchUtils {

	private final static Long ZERO = 0L;

	@SuppressWarnings("unchecked")
	public static <K> IndexInfo<K> getInfo(List<Object> infoList) {
		Map<String, Object> map = new HashMap<>();
		for (int i = 0; i < (infoList.size() / 2); i++) {
			map.put((String) infoList.get(i * 2), infoList.get(i * 2 + 1));
		}
		return IndexInfo.<K>builder().indexName(getString(map.get("index_name")))
				.indexOptions((List<Object>) map.get("index_options")).fields(fields(map.get("fields")))
				.numDocs((Double) map.get("num_docs")).maxDocId(getString(map.get("max_doc_id")))
				.numTerms(getLong(map, "num_terms")).numRecords(getLong(map, "num_records"))
				.invertedSizeMb((Double) map.get("inverted_sz_mb"))
				.totalInvertedIndexBlocks(getLong(map, "total_inverted_index_blocks"))
				.offsetVectorsSizeMb((Double) map.get("offset_vectors_sz_mb"))
				.docTableSizeMb((Double) map.get("doc_table_size_mb"))
				.sortableValuesSizeMb((Double) map.get("sortable_values_size_mb"))
				.keyTableSizeMb((Double) map.get("key_table_size_mb"))
				.recordsPerDocAvg((Double) map.get("records_per_doc_avg"))
				.bytesPerRecordAvg((Double) map.get("bytes_per_record_avg"))
				.offsetsPerTermAvg((Double) map.get("offsets_per_term_avg"))
				.offsetBitsPerRecordAvg((Double) map.get("offset_bits_per_record_avg"))
				.gcStats((List<Object>) map.get("gc_stats")).cursorStats((List<Object>) map.get("cursor_stats"))
				.build();
	}

	@SuppressWarnings("unchecked")
	private static <K> K getString(Object object) {
		if (object != null) {
			if (object instanceof String) {
				return (K) object;
			}
			if (ZERO.equals(object)) {
				return null;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static <K> List<Field<K>> fields(Object object) {
		List<Field<K>> fields = new ArrayList<>();
		for (Object infoObject : (List<Object>) object) {
			List<Object> info = (List<Object>) infoObject;
			K name = (K) info.get(0);
			CommandKeyword type = CommandKeyword.valueOf((String) info.get(2));
			Field<K> field = field(name, type, info);
			for (Object attribute : info.subList(3, info.size())) {
				if (NOINDEX.name().equals(attribute)) {
					field.setNoIndex(true);
				}
				if (SORTABLE.name().equals(attribute)) {
					field.setSortable(true);
				}
			}
			fields.add(field);
		}
		return fields;
	}

	private static <K> Field<K> field(K name, CommandKeyword type, List<Object> info) {
		switch (type) {
		case GEO:
			return GeoField.<K>builder().name(name).build();
		case NUMERIC:
			return NumericField.<K>builder().name(name).build();
		case TAG:
			return TagField.<K>builder().name(name).separator((String) info.get(4)).build();
		default:
			return TextField.<K>builder().name(name).weight((Double) info.get(4))
					.noStem(NOSTEM.name().equals(info.get(info.size() - 1))).build();
		}
	}

	private static Long getLong(Map<String, Object> map, String key) {
		if (map.containsKey(key)) {
			Object value = map.get(key);
			if (value != null) {
				if (value instanceof Long) {
					return (Long) value;
				}
				if (value instanceof String) {
					String string = (String) value;
					if (string.length() > 0) {
						try {
							return Long.parseLong(string);
						} catch (NumberFormatException e) {
							// ignore
						}
					}
				}
			}
		}
		return null;
	}

	public static String escapeTag(String value) {
		return value.replaceAll("([^a-zA-Z0-9])", "\\\\$1");
	}

}
