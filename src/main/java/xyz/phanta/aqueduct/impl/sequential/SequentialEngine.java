package xyz.phanta.aqueduct.impl.sequential;

import xyz.phanta.aqueduct.ComputationFailedException;
import xyz.phanta.aqueduct.engine.IDuctEngine;
import xyz.phanta.aqueduct.engine.PolicyDefinitions;
import xyz.phanta.aqueduct.graph.DuctGraph;
import xyz.phanta.aqueduct.graph.edge.DuctEdge;
import xyz.phanta.aqueduct.graph.edge.EdgeMode;
import xyz.phanta.aqueduct.graph.node.DuctNode;
import xyz.phanta.aqueduct.graph.node.NodeAttribute;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class SequentialEngine<R> implements IDuctEngine<R> {

    private final DuctGraph<R, SequentialEngine<R>> graph;
    private final PolicyDefinitions policies;

    public SequentialEngine(DuctGraph<R, SequentialEngine<R>> graph, PolicyDefinitions policies) {
        this.graph = graph;
        this.policies = policies;
    }

    @Override
    public R computeBlocking() {
        //noinspection unchecked
        Set<DuctNode<R>>[] dirtySet = new Set[] { new HashSet<>(graph.getNodes()), new HashSet<>() };
        int activeSet = 0;
        while (true) {
            int inactiveSet = activeSet ^ 1;
            if (Thread.interrupted()) throw new ComputationFailedException("Interrupted!");
            for (DuctNode<R> node : dirtySet[activeSet]) {
                if (node.hasAttrib(NodeAttribute.GREEDY)) {
                    if (node.checkInputs()) {
                        do {
                            Optional<R> result = node.getExecutor().execute(node.dequeueInputs(), node.getOutputs());
                            if (result.isPresent()) return result.get();
                        } while (node.checkInputs());
                        dirtySet[inactiveSet].add(node);
                    }
                } else {
                    if (node.checkInputs()) {
                        Optional<R> result = node.getExecutor().execute(node.dequeueInputs(), node.getOutputs());
                        if (result.isPresent()) return result.get();
                        dirtySet[inactiveSet].add(node);
                    }
                }
            }
            for (DuctEdge<?> edge : graph.getEdges()) {
                if (updateEdge(edge)) {
                    //noinspection unchecked
                    dirtySet[inactiveSet].add((DuctNode<R>)edge.getDestination().getOwner());
                }
            }
            dirtySet[activeSet].clear();
            activeSet = inactiveSet;
        }
    }
    
    private <T> boolean updateEdge(DuctEdge<T> edge) {
        EdgeMode mode = edge.getEdgeMode();
        if (mode == EdgeMode.TRANSFER_ONE) {
            if (!edge.getSource().isDataAvailable()) return false;
            edge.getDestination().enqueue(edge.getSource().dequeue());
        } else if (mode == EdgeMode.TRANSFER_ALL) {
            List<T> data = edge.getSource().dequeueAll();
            if (data.isEmpty()) return false;
            edge.getDestination().enqueue(data);
        } else {
            List<T> data = edge.getSource().dequeueSome(mode.getCount());
            if (data.isEmpty()) return false;
            edge.getDestination().enqueue(data);
        }
        return true;
    }

}
