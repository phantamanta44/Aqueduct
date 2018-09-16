package xyz.phanta.aqueduct.predef.source;

import xyz.phanta.aqueduct.graph.node.INodeExecutor;
import xyz.phanta.aqueduct.graph.node.IOutput;

import javax.annotation.Nonnegative;
import java.util.List;
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
    public Optional<R> execute(List<List<?>> params, List<? extends IOutput> outputs) {
        if (value < limit) {
            outputs.get(0).write(value);
            value += step;
        }
        return Optional.empty();
    }

}
