package xyz.phanta.aqueduct.predef.terminal;

import xyz.phanta.aqueduct.graph.node.INodeExecutor;
import xyz.phanta.aqueduct.graph.node.INonTerminalExecutor;

import javax.annotation.Nonnegative;

public class TerminalNodes {

    public static <R> INodeExecutor<R> limited(@Nonnegative int iterations,
                                               INodeExecutor<R> delegate, INodeExecutor<R> finished) {
        return new LimitedTerminalNode<>(iterations, delegate, finished);
    }

    public static <R> INodeExecutor<R> limited(@Nonnegative int iterations,
                                               INonTerminalExecutor<R> delegate, INodeExecutor<R> finished) {
        return limited(iterations, (INodeExecutor<R>)delegate, finished);
    }

}
