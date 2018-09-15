package xyz.phanta.aqueduct.graph.node;

import java.util.List;
import java.util.Optional;

@FunctionalInterface
public interface INonTerminalExecutor<R> extends INodeExecutor<R> {

    void executeNonTerminal(List<List<?>> params, List<? extends IOutput> outputs);

    default Optional<R> execute(List<List<?>> params, List<? extends IOutput> outputs) {
        executeNonTerminal(params, outputs);
        return Optional.empty();
    }

}
