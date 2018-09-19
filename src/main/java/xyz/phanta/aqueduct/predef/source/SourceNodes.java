package xyz.phanta.aqueduct.predef.source;

import xyz.phanta.aqueduct.execution.INodeExecutor;

import javax.annotation.Nonnegative;
import java.util.*;
import java.util.function.UnaryOperator;

public class SourceNodes {

    public static <R> INodeExecutor<R> limited(@Nonnegative int iterations, INodeExecutor<R> delegate) {
        return new LimitedSourceNode<>(iterations, delegate);
    }

    public static <T, R> INodeExecutor<R> ofValues(T... values) {
        return ofValues(Arrays.asList(values));
    }

    public static <T, R> INodeExecutor<R> ofValues(Collection<T> values) {
        return limited(1, (params, outputs) -> {
            outputs.putMany((values instanceof List) ? (List)values : new LinkedList<>(values));
            return Optional.empty();
        });
    }

    public static <T, R> INodeExecutor<R> iterate(T initial, UnaryOperator<T> mapper) {
        return new IteratingSourceNode<>(initial, mapper);
    }

    public static <T, R> INodeExecutor<R> iterateLimited(@Nonnegative int iterations,
                                                         T initial, UnaryOperator<T> mapper) {
        return limited(iterations, iterate(initial, mapper));
    }

    public static <R> INodeExecutor<R> ints(int from, @Nonnegative int step) {
        return iterate(from, i -> i + step);
    }

    public static <R> INodeExecutor<R> ints(int from) {
        return ints(from, 1);
    }

    public static <R> INodeExecutor<R> intsLimited(int from, int to, @Nonnegative int step) {
        return new IntIteratingSourceNode<>(from, to, step);
    }

    public static <R> INodeExecutor<R> intsLimited(int from, int to) {
        return intsLimited(from, to, 1);
    }

}
