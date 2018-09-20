package xyz.phanta.aqueduct.predef.source;

import xyz.phanta.aqueduct.execution.INodeExecutor;
import xyz.phanta.aqueduct.execution.Outputs;
import xyz.phanta.aqueduct.execution.Parameters;

import java.util.Optional;
import java.util.function.UnaryOperator;

class IteratingSourceNode<T, R> implements INodeExecutor<R> {

    private final UnaryOperator<T> mapper;

    private T value;

    IteratingSourceNode(T initial, UnaryOperator<T> mapper) {
        this.mapper = mapper;
        this.value = initial;
    }

    @Override
    public Optional<R> execute(Parameters params, Outputs outputs) {
        outputs.put(value);
        value = mapper.apply(value);
        return Optional.empty();
    }

}
