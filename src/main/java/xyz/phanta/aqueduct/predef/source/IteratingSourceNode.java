package xyz.phanta.aqueduct.predef.source;

import xyz.phanta.aqueduct.graph.node.INonTerminalExecutor;
import xyz.phanta.aqueduct.graph.node.IOutput;

import java.util.List;
import java.util.function.UnaryOperator;

public class IteratingSourceNode<T, R> implements INonTerminalExecutor<R> {

    private final UnaryOperator<T> mapper;

    private T value;

    public IteratingSourceNode(T initial, UnaryOperator<T> mapper) {
        this.mapper = mapper;
        this.value = initial;
    }

    @Override
    public void executeNonTerminal(List<List<?>> params, List<? extends IOutput> outputs) {
        outputs.get(0).write(value);
        value = mapper.apply(value);
    }

}
