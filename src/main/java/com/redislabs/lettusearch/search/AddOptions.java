package com.redislabs.lettusearch.search;

import static com.redislabs.lettusearch.protocol.CommandKeyword.IF;
import static com.redislabs.lettusearch.protocol.CommandKeyword.LANGUAGE;
import static com.redislabs.lettusearch.protocol.CommandKeyword.NOSAVE;
import static com.redislabs.lettusearch.protocol.CommandKeyword.PARTIAL;
import static com.redislabs.lettusearch.protocol.CommandKeyword.REPLACE;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddOptions implements RediSearchArgument {

    private boolean noSave;
    private boolean replace;
    private boolean replacePartial;
    private Language language;
    private String ifCondition;

    @Override
    public <K,V> void build(RediSearchCommandArgs<K, V> args) {
        if (noSave) {
            args.add(NOSAVE);
        }
        if (replace) {
            args.add(REPLACE);
            if (replacePartial) {
                args.add(PARTIAL);
            }
        }
        if (language != null) {
            args.add(LANGUAGE);
            args.add(language.name().toLowerCase());
        }
        if (ifCondition != null) {
            args.add(IF);
            args.add(ifCondition);
        }
    }

}
