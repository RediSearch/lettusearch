package com.redislabs.lettusearch.aggregate;

import lombok.Builder;
import lombok.Data;

@Builder
public @Data class CursorArgs {

	private long cursor;
	private Long count;
}
