package xyz.phanta.aqueduct.predef.source;

import xyz.phanta.aqueduct.graph.node.INodeExecutor;
import xyz.phanta.aqueduct.graph.node.IOutput;

import javax.annotation.Nonnegative;
import java.util.List;
import java.util.Optional;

public class LimitedSourceNode<R> implements INodeExecutor<R> {

    private final INodeExecutor<R> delegate;

    @Nonnegative
    private int iterations;

    public LimitedSourceNode(@Nonnegative int iterations, INodeExecutor<R> delegate) {
        this.delegate = delegate;
        this.iterations = iterations;
    }

    @Override
    public Optional<R> execute(List<List<?>> params, List<? extends IOutput> outputs) {
        if (iterations > 0) {
            --iterations;
            return delegate.execute(params, outputs);
        }
        return Optional.empty();
    }

}
