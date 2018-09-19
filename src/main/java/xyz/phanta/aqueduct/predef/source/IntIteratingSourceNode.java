package xyz.phanta.aqueduct.predef.source;

import xyz.phanta.aqueduct.execution.INodeExecutor;
import xyz.phanta.aqueduct.execution.Outputs;
import xyz.phanta.aqueduct.execution.Parameters;

import javax.annotation.Nonnegative;
import java.util.Optional;

public class IntIteratingSourceNode<R> implements INodeExecutor<R> {

    private final int limit;
    @Nonnegative
    private final int step;

    private int value;

    public IntIteratingSourceNode(int initial, int limit, @Nonnegative int step) {
        this.limit = limit;
        this.step = step;
        this.value = initial;
    }

    @Override
    public Optional<R> execute(Parameters params, Outputs outputs) {
        if (value < limit) {
            outputs.put(value);
            value += step;
        }
        return Optional.empty();
    }

}
