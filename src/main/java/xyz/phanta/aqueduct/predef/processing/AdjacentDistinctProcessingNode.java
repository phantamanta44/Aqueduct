package xyz.phanta.aqueduct.predef.processing;

import xyz.phanta.aqueduct.execution.INodeExecutor;
import xyz.phanta.aqueduct.execution.Outputs;
import xyz.phanta.aqueduct.execution.Parameters;

import javax.annotation.Nullable;
import java.util.Optional;

class AdjacentDistinctProcessingNode<R> implements INodeExecutor<R> {

    @Nullable
    private Object prev = null;

    @Override
    public Optional<R> execute(Parameters params, Outputs outputs) {
        Object value = params.val();
        if (!value.equals(prev)) {
            prev = value;
            outputs.put(value);
        }
        return Optional.empty();
    }

}
