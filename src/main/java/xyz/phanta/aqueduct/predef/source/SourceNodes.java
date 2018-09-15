package xyz.phanta.aqueduct.predef.source;

import xyz.phanta.aqueduct.graph.node.INodeExecutor;
import xyz.phanta.aqueduct.graph.node.INonTerminalExecutor;

import javax.annotation.Nonnegative;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.UnaryOperator;

public class SourceNodes {

    public static <R> INodeExecutor<R> limited(@Nonnegative int iterations, INodeExecutor<R> delegate) {
        return new LimitedSourceNode<>(iterations, delegate);
    }

    public static <R> INodeExecutor<R> limited(@Nonnegative int iterations, INonTerminalExecutor<R> delegate) {
        return limited(iterations, (INodeExecutor<R>)delegate);
    }

    public static <R, T> INodeExecutor<R> ofValues(T... values) {
        return ofValues(Arrays.asList(values));
    }

    public static <R, T> INodeExecutor<R> ofValues(Collection<T> values) {
        return limited(1, (params, outputs) -> outputs.get(0).write(values));
    }

    public static <R, T> INodeExecutor<R> iterate(T initial, UnaryOperator<T> mapper) {
        return new IteratingSourceNode<>(initial, mapper);
    }

    public static <R, T> INodeExecutor<R> iterateLimited(@Nonnegative int iterations,
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
