package com.redislabs.lettusearch;

import com.redislabs.lettusearch.impl.protocol.CommandKeyword;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.redislabs.lettusearch.impl.protocol.CommandKeyword.*;

public class RediSearchUtils {

    private final static Long ZERO = 0L;

    @SuppressWarnings("unchecked")
    public static <K> IndexInfo<K> getInfo(List<Object> infoList) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < (infoList.size() / 2); i++) {
            map.put((String) infoList.get(i * 2), infoList.get(i * 2 + 1));
        }
        return IndexInfo.<K>builder().indexName(getString(map.get("index_name"))).indexOptions((List<Object>) map.get("index_options")).fields(fields(map.get("fields"))).numDocs(getDouble(map.get("num_docs"))).maxDocId(getString(map.get("max_doc_id"))).numTerms(getLong(map, "num_terms")).numRecords(getLong(map, "num_records")).invertedSizeMb(getDouble(map.get("inverted_sz_mb"))).totalInvertedIndexBlocks(getLong(map, "total_inverted_index_blocks")).offsetVectorsSizeMb(getDouble(map.get("offset_vectors_sz_mb"))).docTableSizeMb(getDouble(map.get("doc_table_size_mb"))).sortableValuesSizeMb(getDouble(map.get("sortable_values_size_mb"))).keyTableSizeMb(getDouble(map.get("key_table_size_mb"))).recordsPerDocAvg(getDouble(map.get("records_per_doc_avg"))).bytesPerRecordAvg(getDouble(map.get("bytes_per_record_avg"))).offsetsPerTermAvg(getDouble(map.get("offsets_per_term_avg"))).offsetBitsPerRecordAvg(getDouble(map.get("offset_bits_per_record_avg"))).gcStats((List<Object>) map.get("gc_stats")).cursorStats((List<Object>) map.get("cursor_stats")).build();
    }

    private static Double getDouble(Object object) {
        return (Double) object;
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
                return Field.Geo.builder(name).build();
            case NUMERIC:
                return Field.Numeric.builder(name).build();
            case TAG:
                return Field.Tag.builder(name).separator((String) info.get(4)).build();
            default:
                return Field.Text.builder(name).weight((Double) info.get(4)).noStem(NOSTEM.name().equals(info.get(info.size() - 1))).build();
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
