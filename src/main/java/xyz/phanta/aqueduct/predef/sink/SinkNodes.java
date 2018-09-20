package xyz.phanta.aqueduct.predef.sink;

import xyz.phanta.aqueduct.execution.INodeExecutor;
import xyz.phanta.aqueduct.execution.Parameters;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class SinkNodes {

    public static <T, R> INodeExecutor<R> forEach(Consumer<T> consumer) {
        return (params, outputs) -> {
            consumer.accept(params.val());
            return Optional.empty();
        };
    }

    public static <T, R> INodeExecutor<R> forEachMany(Consumer<List<T>> consumer) {
        return (params, outputs) -> {
            consumer.accept(params.vals());
            return Optional.empty();
        };
    }

    public static <R> INodeExecutor<R> forEachRaw(Consumer<Parameters> consumer) {
        return (params, outputs) -> {
            consumer.accept(params);
            return Optional.empty();
        };
    }

}
