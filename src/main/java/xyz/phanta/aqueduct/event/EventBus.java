package xyz.phanta.aqueduct.event;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
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

    public void subscribe(Consumer<T> callback) {
        callbacks.add(callback);
    }

    public void returnEvent(T event) {
        CompletableFuture<T> promise = promises.remove(event);
        if (promise != null) promise.complete(event);
    }

}
