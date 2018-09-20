package xyz.phanta.aqueduct.execution;

import xyz.phanta.aqueduct.graph.node.DuctNode;

import java.util.Optional;

@FunctionalInterface
public interface INodeExecutor<R> {

    default void init(DuctNode<R> node) {
        // NO-OP
    }

    Optional<R> execute(Parameters params, Outputs outputs);

}
