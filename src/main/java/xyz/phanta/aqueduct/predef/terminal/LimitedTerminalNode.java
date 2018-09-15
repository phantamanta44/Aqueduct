package xyz.phanta.aqueduct.predef.terminal;

import xyz.phanta.aqueduct.ComputationFailedException;
import xyz.phanta.aqueduct.graph.node.INodeExecutor;
import xyz.phanta.aqueduct.graph.node.IOutput;

import javax.annotation.Nonnegative;
import java.util.List;
import java.util.Optional;

public class LimitedTerminalNode<R> implements INodeExecutor<R> {

    private final INodeExecutor<R> delegate;
    private final INodeExecutor<R> finisher;

    @Nonnegative
    private int iterations;

    public LimitedTerminalNode(@Nonnegative int iterations, INodeExecutor<R> delegate, INodeExecutor<R> finisher) {
        this.delegate = delegate;
        this.finisher = finisher;
        this.iterations = iterations;
    }

    @Override
    public Optional<R> execute(List<List<?>> params, List<? extends IOutput> outputs) {
        if (--iterations == 0) {
            return delegate.execute(params, outputs).map(Optional::of)
                    .orElse(finisher.execute(params, outputs).map(Optional::of)
                            .orElseThrow(() -> new ComputationFailedException("Finisher didn't finish!")));
        }
        return delegate.execute(params, outputs);
    }

}
