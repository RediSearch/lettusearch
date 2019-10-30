package com.redislabs.lettusearch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

public class RediSearchUtils {

	@Data
	public static class Info {
		private String indexName;
		private Long numDocs;
		private List<Object> indexOptions;
		private List<Object> fields;
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
	public static Info getInfo(List<Object> infoList) {
		Map<String, Object> map = new HashMap<>();
		for (int i = 0; i < (infoList.size() / 2); i++) {
			map.put((String) infoList.get(i * 2), infoList.get(i * 2 + 1));
		}
		Info info = new Info();
		info.setIndexName((String) map.get("index_name"));
		info.setIndexOptions((List<Object>) map.get("index_options"));
		info.setFields((List<Object>) map.get("fields"));
		info.setNumDocs(getLong(map, "num_docs"));
		info.setMaxDocId((String) map.get("max_doc_id"));
		info.setNumTerms(getLong(map, "num_terms"));
		info.setNumRecords(getLong(map, "num_records"));
		info.setInvertedSizeMb(getDouble(map, "inverted_sz_mb"));
		info.setTotalInvertedIndexBlocks(getLong(map, "total_inverted_index_blocks"));
		info.setOffsetVectorsSizeMb(getDouble(map, "offset_vectors_sz_mb"));
		info.setDocTableSizeMb(getDouble(map, "doc_table_size_mb"));
		info.setSortableValuesSizeMb(getDouble(map, "sortable_values_size_mb"));
		info.setKeyTableSizeMb(getDouble(map, "key_table_size_mb"));
		info.setRecordsPerDocAvg(getDouble(map, "records_per_doc_avg"));
		info.setBytesPerRecordAvg(getDouble(map, "bytes_per_record_avg"));
		info.setOffsetsPerTermAvg(getDouble(map, "offsets_per_term_avg"));
		info.setOffsetBitsPerRecordAvg(getDouble(map, "offset_bits_per_record_avg"));
		info.setGcStats((List<Object>) map.get("gc_stats"));
		info.setCursorStats((List<Object>) map.get("cursor_stats"));
		return info;
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
