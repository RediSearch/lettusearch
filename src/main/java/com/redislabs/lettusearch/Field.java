package com.redislabs.lettusearch;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Field {

	public static enum FieldType {
		TEXT, NUMERIC, GEO
	}

	@NonNull
	String name;
	@NonNull
	FieldType type;
	Double weight;
	boolean sortable;
	boolean noStem;
	boolean noIndex;
}
