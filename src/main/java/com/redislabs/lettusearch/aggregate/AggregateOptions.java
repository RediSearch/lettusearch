package com.redislabs.lettusearch.aggregate;

import com.redislabs.lettusearch.RediSearchArgument;
import com.redislabs.lettusearch.protocol.RediSearchCommandArgs;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

import static com.redislabs.lettusearch.protocol.CommandKeyword.LOAD;

@Data
@Builder
public class AggregateOptions implements RediSearchArgument {

    @Singular
    private List<String> loads;
    @Singular
    private List<Operation> operations;

    @Override
    public <K, V> void build(RediSearchCommandArgs<K, V> args) {
        if (!loads.isEmpty()) {
            args.add(LOAD);
            args.add(loads.size());
            loads.forEach(args::addProperty);
        }
        operations.forEach(o -> o.build(args));
    }

}
