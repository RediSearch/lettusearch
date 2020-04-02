package com.redislabs.lettusearch.index;

import static com.redislabs.lettusearch.protocol.CommandKeyword.MAXTEXTFIELDS;
import static com.redislabs.lettusearch.protocol.CommandKeyword.NOFIELDS;
import static com.redislabs.lettusearch.protocol.CommandKeyword.NOFREQS;
import static com.redislabs.lettusearch.protocol.CommandKeyword.NOHL;
import static com.redislabs.lettusearch.protocol.CommandKeyword.NOOFFSETS;
import static com.redislabs.lettusearch.protocol.CommandKeyword.STOPWORDS;
import static com.redislabs.lettusearch.protocol.CommandKeyword.TEMPORARY;

import java.util.List;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;

@Builder
public @Data class CreateOptions implements RediSearchArgument {

	private boolean maxTextFields;
	private Long temporary;
	private boolean noOffsets;
	private boolean noHL;
	private boolean noFields;
	private boolean noFreqs;
	/**
	 * set to empty list for STOPWORDS 0
	 */
	private List<String> stopWords;

	@Override
	public <K, V> void build(RediSearchCommandArgs<K, V> args) {
		if (maxTextFields) {
			args.add(MAXTEXTFIELDS);
		}
		if (temporary != null) {
			args.add(TEMPORARY);
			args.add(temporary);
		}
		if (noOffsets) {
			args.add(NOOFFSETS);
		}
		if (noHL) {
			args.add(NOHL);
		}
		if (noFields) {
			args.add(NOFIELDS);
		}
		if (noFreqs) {
			args.add(NOFREQS);
		}
		if (stopWords != null) {
			args.add(STOPWORDS);
			args.add(stopWords.size());
			stopWords.forEach(args::add);
		}
	}

}
