package xyz.phanta.aqueduct.predef.processing;

import xyz.phanta.aqueduct.execution.INodeExecutor;
import xyz.phanta.aqueduct.execution.Outputs;
import xyz.phanta.aqueduct.execution.Parameters;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class DistinctProcessingNode<R> implements INodeExecutor<R> {

    private final Set<Object> seen = new HashSet<>();

    @Override
    public Optional<R> execute(Parameters params, Outputs outputs) {
        Object value = params.val();
        if (!seen.contains(value)) {
            seen.add(value);
            outputs.put(value);
        }
        return Optional.empty();
    }

}
