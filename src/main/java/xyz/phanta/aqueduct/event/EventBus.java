package xyz.phanta.aqueduct.event;

import xyz.phanta.aqueduct.execution.INodeExecutor;
import xyz.phanta.aqueduct.execution.Outputs;
import xyz.phanta.aqueduct.execution.Parameters;
import xyz.phanta.aqueduct.graph.node.DuctNode;
import xyz.phanta.aqueduct.predef.sink.SinkNodes;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class EventBus<T> {

    private final Collection<Consumer<T>> callbacks = new LinkedList<>();
    private final Map<T, CompletableFuture<T>> promises = new HashMap<>();

    public void publish(T event) {
        callbacks.forEach(cb -> cb.accept(event));
    }

    public CompletableFuture<T> compute(T event) {
        CompletableFuture<T> promise = new CompletableFuture<>();
        promises.put(event, promise);
        publish(event);
        return promise;
    }

    private void subscribe(Consumer<T> callback) {
        callbacks.add(callback);
    }

    private void returnEvent(T event) {
        CompletableFuture<T> promise = promises.remove(event);
        if (promise != null) promise.complete(event);
    }

    public <R> INodeExecutor<R> createSource() {
        return new EventSource<>();
    }

    public <R> INodeExecutor<R> createSink() {
        return SinkNodes.forEach(this::returnEvent);
    }

    private class EventSource<R> implements INodeExecutor<R> {

        @Override
        public void init(DuctNode<R> node) {
            subscribe(event -> node.getOutputs().get(0).write(event));
        }

        @Override
        public Optional<R> execute(Parameters params, Outputs outputs) {
            return Optional.empty();
        }

    }

}
