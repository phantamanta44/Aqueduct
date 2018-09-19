package xyz.phanta.aqueduct.execution;

import java.util.Optional;

@FunctionalInterface
public interface INodeExecutor<R> {

    Optional<R> execute(Parameters params, Outputs outputs);

}
