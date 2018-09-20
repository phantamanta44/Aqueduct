package xyz.phanta.aqueduct.predef.terminal;

import xyz.phanta.aqueduct.execution.INodeExecutor;
import xyz.phanta.aqueduct.execution.Parameters;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class TerminalNodes {

    public static <R> INodeExecutor<R> limited(int iterations,
                                               INodeExecutor<R> delegate, INodeExecutor<R> finished) {
        return new LimitedTerminalNode<>(iterations, delegate, finished);
    }

    public static <R> INodeExecutor<R> filter(Predicate<R> pred) {
        return (params, output) -> {
            R value = params.val();
            return pred.test(value) ? Optional.of(value) : Optional.empty();
        };
    }

    public static <T, R> INodeExecutor<R> map(Function<T, R> mapper) {
        return (params, outputs) -> Optional.of(mapper.apply(params.val()));
    }

    public static <T, R> INodeExecutor<R> filterMap(Predicate<T> pred, Function<T, R> mapper) {
        return (params, outputs) -> {
            T value = params.val();
            return pred.test(value) ? Optional.of(mapper.apply(value)) : Optional.empty();
        };
    }

    public static <T, R> INodeExecutor<R> maybeMap(Function<T, Optional<R>> mapper) {
        return (params, outputs) -> mapper.apply(params.val());
    }

    public static <T, R> INodeExecutor<R> mapMany(Function<List<T>, R> mapper) {
        return (params, outputs) -> Optional.of(mapper.apply(params.vals()));
    }

    public static <R> INodeExecutor<R> mapRaw(Function<Parameters, R> mapper) {
        return (params, outputs) -> Optional.of(mapper.apply(params));
    }

    public static <R> INodeExecutor<R> maybeMapRaw(Function<Parameters, Optional<R>> mapper) {
        return (params, outputs) -> mapper.apply(params);
    }

    public static <R> INodeExecutor<R> filterRaw(Predicate<Parameters> pred) {
        return (params, outputs) -> pred.test(params) ? Optional.of(params.val()) : Optional.empty();
    }

    public static <R> INodeExecutor<R> any() {
        return (params, outputs) -> Optional.of(params.val());
    }

    public static <T, R> INodeExecutor<R> anyMap(Function<T, R> mapper) {
        return (params, outputs) -> Optional.of(mapper.apply(params.val()));
    }

    public static <T, R> INodeExecutor<R> fold(Function<List<T>, R> folder) {
        return (params, outputs) -> Optional.of(folder.apply(params.vals()));
    }

    public static <R> INodeExecutor<List<R>> collect() {
        return (params, outputs) -> Optional.of(params.vals());
    }

}
