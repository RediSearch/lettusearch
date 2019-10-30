package com.redislabs.lettusearch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

public class RediSearchUtils {

	@Data
	public static class Info {
		private String indexName;
		private long numDocs;
		private List<Object> indexOptions;
		private List<Object> fields;
		private String maxDocId;
		private long numTerms;
		private long numRecords;
		private double invertedSizeMb;
		private long totalInvertedIndexBlocks;
		private double offsetVectorsSizeMb;
		private double docTableSizeMb;
		private double sortableValuesSizeMb;
		private double keyTableSizeMb;
		private double recordsPerDocAvg;
		private double bytesPerRecordAvg;
		private double offsetsPerTermAvg;
		private double offsetBitsPerRecordAvg;
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
		info.setNumDocs(Long.parseLong((String) map.get("num_docs")));
		info.setMaxDocId((String) map.get("max_doc_id"));
		info.setNumTerms(Long.parseLong((String) map.get("num_terms")));
		info.setNumRecords(Long.parseLong((String) map.get("num_records")));
		info.setInvertedSizeMb(Double.parseDouble((String) map.get("inverted_sz_mb")));
		info.setTotalInvertedIndexBlocks(Long.parseLong((String) map.get("total_inverted_index_blocks")));
		info.setOffsetVectorsSizeMb(Double.parseDouble((String) map.get("offset_vectors_sz_mb")));
		info.setDocTableSizeMb(Double.parseDouble((String) map.get("doc_table_size_mb")));
		info.setSortableValuesSizeMb(Double.parseDouble((String) map.get("sortable_values_size_mb")));
		info.setKeyTableSizeMb(Double.parseDouble((String) map.get("key_table_size_mb")));
		info.setRecordsPerDocAvg(Double.parseDouble((String) map.get("records_per_doc_avg")));
		info.setBytesPerRecordAvg(Double.parseDouble((String) map.get("bytes_per_record_avg")));
		info.setOffsetsPerTermAvg(Double.parseDouble((String) map.get("offsets_per_term_avg")));
		info.setOffsetBitsPerRecordAvg(Double.parseDouble((String) map.get("offset_bits_per_record_avg")));
		info.setGcStats((List<Object>) map.get("gc_stats"));
		info.setCursorStats((List<Object>) map.get("cursor_stats"));
		return info;
	}

}
