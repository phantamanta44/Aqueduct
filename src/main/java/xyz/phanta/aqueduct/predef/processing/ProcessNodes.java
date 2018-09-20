package xyz.phanta.aqueduct.predef.processing;

import xyz.phanta.aqueduct.execution.INodeExecutor;
import xyz.phanta.aqueduct.execution.Parameters;
import xyz.phanta.aqueduct.util.DistribStrategy;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

public class ProcessNodes {

    public static <T, R> INodeExecutor<R> filter(Predicate<T> pred) {
        return (params, outputs) -> {
            T value = params.val();
            if (pred.test(value)) outputs.put(value);
            return Optional.empty();
        };
    }

    public static <T, R> INodeExecutor<R> map(Function<T, ?> mapper) {
        return (params, outputs) -> {
            outputs.put(mapper.apply(params.val()));
            return Optional.empty();
        };
    }

    public static <T, R> INodeExecutor<R> filterMap(Predicate<T> pred, Function<T, ?> mapper) {
        return (params, outputs) -> {
            T value = params.val();
            if (pred.test(value)) outputs.put(mapper.apply(value));
            return Optional.empty();
        };
    }

    public static <T, R> INodeExecutor<R> maybeMap(Function<T, Optional<?>> mapper) {
        return (params, outputs) -> {
            mapper.apply(params.val()).ifPresent(outputs::put);
            return Optional.empty();
        };
    }

    public static <T, R> INodeExecutor<R> mapMany(Function<List<T>, ?> mapper) {
        return (params, outputs) -> {
            outputs.put(mapper.apply(params.vals()));
            return Optional.empty();
        };
    }

    public static <R> INodeExecutor<R> mapRaw(Function<Parameters, ?> mapper) {
        return (params, outputs) -> {
            outputs.put(mapper.apply(params));
            return Optional.empty();
        };
    }

    public static <R> INodeExecutor<R> maybeMapRaw(Function<Parameters, Optional<?>> mapper) {
        return (params, outputs) -> {
            mapper.apply(params).ifPresent(outputs::put);
            return Optional.empty();
        };
    }

    public static <R> INodeExecutor<R> filterRaw(Predicate<Parameters> pred) {
        return (params, outputs) -> {
            if (pred.test(params)) outputs.put(params.val());
            return Optional.empty();
        };
    }

    public static <T, R> INodeExecutor<R> fold(BinaryOperator<T> reducer) {
        return (params, outputs) -> {
            Iterator<T> values = params.<T>vals().iterator();
            T value = values.next();
            while (values.hasNext()) {
                value = reducer.apply(value, values.next());
            }
            outputs.put(value);
            return Optional.empty();
        };
    }

    public static <T, U, R> INodeExecutor<R> fold(T identity, BiFunction<T, U, T> folder) {
        return (params, outputs) -> {
            T value = identity;
            for (U element : params.<U>vals()) {
                value = folder.apply(value, element);
            }
            outputs.put(value);
            return Optional.empty();
        };
    }

    public static <R> INodeExecutor<R> collect() {
        return (params, outputs) -> {
            outputs.put(params.vals());
            return Optional.empty();
        };
    }

    public static <T, U, R> INodeExecutor<R> join(BiFunction<T, U, ?> joiner) {
        return (params, outputs) -> {
            outputs.put(joiner.apply(params.valAt(0), params.valAt(1)));
            return Optional.empty();
        };
    }

    public static <R> INodeExecutor<R> zip() {
        return (params, outputs) -> {
            outputs.put(params.valAt(0));
            outputs.put(params.valAt(1));
            return Optional.empty();
        };
    }

    public static <R> INodeExecutor<R> buffer() {
        return (params, outputs) -> {
            outputs.put(params.val());
            return Optional.empty();
        };
    }

    // stateful stuff

    public static <R> INodeExecutor<R> counter() {
        return new CountingProcessingNode<>();
    }

    public static <R> INodeExecutor<R> distinct() {
        return new DistinctProcessingNode<>();
    }

    public static <R> INodeExecutor<R> distinctAdj() {
        return new AdjacentDistinctProcessingNode<>();
    }

    public static <R> INodeExecutor<R> distribute(DistribStrategy strategy) {
        return new DistributingProcessingNode<>(strategy);
    }

    public static <R> INodeExecutor<R> nth(int index) {
        return new NthElementProcessingNode<>(index);
    }

    public static <R> INodeExecutor<R> everyNth(int index) {
        return new EveryNthElementProcessingNode<>(index);
    }

    public static <R> INodeExecutor<R> skip(int count) {
        return new SkippingProcessingNode<>(count);
    }

    public static <R> INodeExecutor<R> limit(int count) {
        return new LimitedProcessingNode<>(count);
    }

}
