package xyz.phanta.aqueduct.predef.source;

import xyz.phanta.aqueduct.execution.INodeExecutor;
import xyz.phanta.aqueduct.execution.Outputs;
import xyz.phanta.aqueduct.execution.Parameters;

import java.util.Optional;

class LimitedSourceNode<R> implements INodeExecutor<R> {

    private final INodeExecutor<R> delegate;

    private int iterations;

    LimitedSourceNode(int iterations, INodeExecutor<R> delegate) {
        this.delegate = delegate;
        this.iterations = iterations;
    }

    @Override
    public Optional<R> execute(Parameters params, Outputs outputs) {
        if (iterations > 0) {
            --iterations;
            return delegate.execute(params, outputs);
        }
        return Optional.empty();
    }

}
