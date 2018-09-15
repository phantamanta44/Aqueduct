package xyz.phanta.aqueduct.engine;

import java.util.concurrent.CompletableFuture;

public interface IDuctEngine<R> {

    R computeBlocking();

    default CompletableFuture<R> compute() {
        return CompletableFuture.supplyAsync(this::computeBlocking);
    }

}
