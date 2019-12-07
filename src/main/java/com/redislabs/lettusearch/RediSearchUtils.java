package com.redislabs.lettusearch;

import static com.redislabs.lettusearch.CommandKeyword.NOINDEX;
import static com.redislabs.lettusearch.CommandKeyword.NOSTEM;
import static com.redislabs.lettusearch.CommandKeyword.SORTABLE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.redislabs.lettusearch.search.field.Field;
import com.redislabs.lettusearch.search.field.TextField;

import lombok.Data;
import lombok.experimental.Accessors;

public class RediSearchUtils {

	@Accessors(fluent = true)
	public static @Data class IndexInfo {
		private String indexName;
		private Long numDocs;
		private List<Object> indexOptions;
		private List<Field> fields;
		private String maxDocId;
		private Long numTerms;
		private Long numRecords;
		private Double invertedSizeMb;
		private Long totalInvertedIndexBlocks;
		private Double offsetVectorsSizeMb;
		private Double docTableSizeMb;
		private Double sortableValuesSizeMb;
		private Double keyTableSizeMb;
		private Double recordsPerDocAvg;
		private Double bytesPerRecordAvg;
		private Double offsetsPerTermAvg;
		private Double offsetBitsPerRecordAvg;
		private List<Object> gcStats;
		private List<Object> cursorStats;
	}

	@SuppressWarnings("unchecked")
	public static IndexInfo getInfo(List<Object> infoList) {
		Map<String, Object> map = new HashMap<>();
		for (int i = 0; i < (infoList.size() / 2); i++) {
			map.put((String) infoList.get(i * 2), infoList.get(i * 2 + 1));
		}
		IndexInfo info = new IndexInfo();
		info.indexName((String) map.get("index_name"));
		info.indexOptions((List<Object>) map.get("index_options"));
		info.fields(fields(map.get("fields")));
		info.numDocs(getLong(map, "num_docs"));
		info.maxDocId((String) map.get("max_doc_id"));
		info.numTerms(getLong(map, "num_terms"));
		info.numRecords(getLong(map, "num_records"));
		info.invertedSizeMb(getDouble(map, "inverted_sz_mb"));
		info.totalInvertedIndexBlocks(getLong(map, "total_inverted_index_blocks"));
		info.offsetVectorsSizeMb(getDouble(map, "offset_vectors_sz_mb"));
		info.docTableSizeMb(getDouble(map, "doc_table_size_mb"));
		info.sortableValuesSizeMb(getDouble(map, "sortable_values_size_mb"));
		info.keyTableSizeMb(getDouble(map, "key_table_size_mb"));
		info.recordsPerDocAvg(getDouble(map, "records_per_doc_avg"));
		info.bytesPerRecordAvg(getDouble(map, "bytes_per_record_avg"));
		info.offsetsPerTermAvg(getDouble(map, "offsets_per_term_avg"));
		info.offsetBitsPerRecordAvg(getDouble(map, "offset_bits_per_record_avg"));
		info.gcStats((List<Object>) map.get("gc_stats"));
		info.cursorStats((List<Object>) map.get("cursor_stats"));
		return info;
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
			String value = (String) map.get(key);
			if (value != null && value.length() > 0) {
				try {
					return Long.parseLong(value);
				} catch (NumberFormatException e) {
					// ignore
				}
			}
		}
		return null;
	}

}
