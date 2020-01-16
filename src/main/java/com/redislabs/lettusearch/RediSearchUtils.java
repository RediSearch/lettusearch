package com.redislabs.lettusearch;

import static com.redislabs.lettusearch.protocol.CommandKeyword.NOINDEX;
import static com.redislabs.lettusearch.protocol.CommandKeyword.NOSTEM;
import static com.redislabs.lettusearch.protocol.CommandKeyword.SORTABLE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redislabs.lettusearch.index.IndexInfo;
import com.redislabs.lettusearch.protocol.CommandKeyword;
import com.redislabs.lettusearch.search.field.Field;
import com.redislabs.lettusearch.search.field.TextField;

public class RediSearchUtils {

	private final static Long ZERO = 0l;

	@SuppressWarnings("unchecked")
	public static IndexInfo getInfo(List<Object> infoList) {
		Map<String, Object> map = new HashMap<>();
		for (int i = 0; i < (infoList.size() / 2); i++) {
			map.put((String) infoList.get(i * 2), infoList.get(i * 2 + 1));
		}
		return IndexInfo.builder().indexName(getString(map.get("index_name")))
				.indexOptions((List<Object>) map.get("index_options")).fields(fields(map.get("fields")))
				.numDocs(getLong(map, "num_docs")).maxDocId(getString(map.get("max_doc_id")))
				.numTerms(getLong(map, "num_terms")).numRecords(getLong(map, "num_records"))
				.invertedSizeMb(getDouble(map, "inverted_sz_mb"))
				.totalInvertedIndexBlocks(getLong(map, "total_inverted_index_blocks"))
				.offsetVectorsSizeMb(getDouble(map, "offset_vectors_sz_mb"))
				.docTableSizeMb(getDouble(map, "doc_table_size_mb"))
				.sortableValuesSizeMb(getDouble(map, "sortable_values_size_mb"))
				.keyTableSizeMb(getDouble(map, "key_table_size_mb"))
				.recordsPerDocAvg(getDouble(map, "records_per_doc_avg"))
				.bytesPerRecordAvg(getDouble(map, "bytes_per_record_avg"))
				.offsetsPerTermAvg(getDouble(map, "offsets_per_term_avg"))
				.offsetBitsPerRecordAvg(getDouble(map, "offset_bits_per_record_avg"))
				.gcStats((List<Object>) map.get("gc_stats")).cursorStats((List<Object>) map.get("cursor_stats"))
				.build();
	}

	private static String getString(Object object) {
		if (object != null) {
			if (object instanceof String) {
				return (String) object;
			}
			if (ZERO.equals(object)) {
				return null;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static List<Field> fields(Object object) {
		List<Field> fields = new ArrayList<>();
		for (Object infoObject : (List<Object>) object) {
			List<Object> info = (List<Object>) infoObject;
			String name = (String) info.get(0);
			CommandKeyword type = CommandKeyword.valueOf((String) info.get(2));
			Field field = field(name, type, info);
			for (Object attribute : info.subList(3, info.size())) {
				if (NOINDEX.name().equals(attribute)) {
					field.noIndex(true);
				}
				if (SORTABLE.name().equals(attribute)) {
					field.sortable(true);
				}
			}
			fields.add(field);
		}
		return fields;
	}

	private static Field field(String name, CommandKeyword type, List<Object> info) {
		switch (type) {
		case GEO:
			return Field.geo(name);
		case NUMERIC:
			return Field.numeric(name);
		case TAG:
			return Field.tag(name).separator((String) info.get(4));
		default:
			TextField text = Field.text(name).weight(Double.parseDouble((String) info.get(4)));
			if (NOSTEM.name().equals(info.get(info.size() - 1))) {
				text.noStem(true);
			}
			return text;
		}
	}

	private static Double getDouble(Map<String, Object> map, String key) {
		if (map.containsKey(key)) {
			String value = (String) map.get(key);
			if (value != null && value.length() > 0) {
				try {
					return Double.parseDouble(value);
				} catch (NumberFormatException e) {
					// ignore
				}
			}
		}
		return null;
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

}
