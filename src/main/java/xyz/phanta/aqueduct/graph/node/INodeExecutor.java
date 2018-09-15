package xyz.phanta.aqueduct.graph.node;

import java.util.List;
import java.util.Optional;

@FunctionalInterface
public interface INodeExecutor<R> {

    Optional<R> execute(List<List<?>> params, List<? extends IOutput> outputs);

}
