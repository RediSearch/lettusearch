package com.redislabs.lettusearch.search;

import lombok.Builder;
import lombok.Data;

@Builder
public @Data class DelArgs<K> {

	private K documentId;
	private boolean deleteDocument;
}
