package xyz.phanta.aqueduct.impl.parallel;

import xyz.phanta.aqueduct.ComputationFailedException;
import xyz.phanta.aqueduct.engine.AbstractDuctEngine;
import xyz.phanta.aqueduct.engine.PolicyDefinitions;
import xyz.phanta.aqueduct.graph.DuctGraph;
import xyz.phanta.aqueduct.graph.edge.DuctEdge;
import xyz.phanta.aqueduct.graph.node.DuctNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class ParallelEngine<R> extends AbstractDuctEngine<R> {

    private final Map<Object, TickableEntity> tickables = new HashMap<>();

    ParallelEngine(DuctGraph<R, ParallelEngine<R>> graph, PolicyDefinitions policies) {
        super(policies);
        for (DuctNode<R> node : graph.getNodes()) tickables.put(node, new NodeWrapper(node));
        for (DuctEdge<?> edge : graph.getEdges()) tickables.put(edge, new EdgeWrapper(edge));
    }

    @Override
    public R computeBlocking() {
        try {
            return compute().get();
        } catch (InterruptedException e) {
            throw new ComputationFailedException(e);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof ComputationFailedException) {
                throw (ComputationFailedException)e.getCause();
            } else {
                throw new ComputationFailedException(e.getCause());
            }
        }
    }

    @Override
    public CompletableFuture<R> compute() {
        beforeCompute();

        ExecutorService threadPool = Executors.newCachedThreadPool();
        CompletableFuture<R> promise = new CompletableFuture<>();
        promise.whenComplete((result, error) -> threadPool.shutdownNow());
        
        for (TickableEntity tickable : tickables.values()) tickable.queueTick(promise, threadPool);
        return promise;
    }

    private abstract class TickableEntity {

        final AtomicBoolean queued = new AtomicBoolean(false);

        void queueTick(CompletableFuture<R> promise, ExecutorService threadPool) {
            if (!queued.getAndSet(true)) threadPool.submit(() -> tick(promise, threadPool));
        }

        abstract void tick(CompletableFuture<R> promise, ExecutorService threadPool);

    }

    private class NodeWrapper extends TickableEntity {

        private final DuctNode<R> node;

        NodeWrapper(DuctNode<R> node) {
            this.node = node;
        }

        @Override
        public void tick(CompletableFuture<R> promise, ExecutorService threadPool) {
            try {
                Optional<R> result = updateNode(node);
                //noinspection OptionalAssignedToNull
                if (result != null) {
                    if (result.isPresent()) {
                        promise.complete(result.get());
                    } else {
                        queued.set(false);
                        node.getOutputs().stream()
                                .flatMap(socket -> socket.getEdges().stream())
                                .map(tickables::get)
                                .forEach(entity -> entity.queueTick(promise, threadPool));
                        queueTick(promise, threadPool);
                    }
                } else {
                    queued.set(false);
                }
            } catch (ComputationFailedException e) {
                promise.completeExceptionally(e);
            } catch (Exception e) {
                promise.completeExceptionally(new ComputationFailedException(e));
            }
        }

    }

    private class EdgeWrapper extends TickableEntity {

        private final DuctEdge<?> edge;

        EdgeWrapper(DuctEdge<?> edge) {
            this.edge = edge;
        }

        @Override
        public void tick(CompletableFuture<R> promise, ExecutorService threadPool) {
            try {
                if (updateEdge(edge)) {
                    queued.set(false);
                    tickables.get(edge.getDestination().getOwner()).queueTick(promise, threadPool);
                    queueTick(promise, threadPool);
                } else {
                    queued.set(false);
                }
            } catch (ComputationFailedException e) {
                promise.completeExceptionally(e);
            } catch (Exception e) {
                promise.completeExceptionally(new ComputationFailedException(e));
            }
        }

    }

}
