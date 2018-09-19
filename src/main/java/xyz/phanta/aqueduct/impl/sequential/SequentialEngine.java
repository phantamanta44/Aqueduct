package xyz.phanta.aqueduct.impl.sequential;

import xyz.phanta.aqueduct.ComputationFailedException;
import xyz.phanta.aqueduct.engine.AbstractDuctEngine;
import xyz.phanta.aqueduct.engine.PolicyDefinitions;
import xyz.phanta.aqueduct.graph.DuctGraph;
import xyz.phanta.aqueduct.graph.edge.DuctEdge;
import xyz.phanta.aqueduct.graph.node.DuctNode;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SequentialEngine<R> extends AbstractDuctEngine<R> {

    private final DuctGraph<R, SequentialEngine<R>> graph;

    SequentialEngine(DuctGraph<R, SequentialEngine<R>> graph, PolicyDefinitions policies) {
        super(policies);
        this.graph = graph;
    }

    @Override
    public R computeBlocking() {
        beforeCompute();

        //noinspection unchecked
        Set<DuctNode<R>>[] dirtySet = new Set[] { new HashSet<>(graph.getNodes()), new HashSet<>() };
        int activeSet = 0;
        while (true) {
            int inactiveSet = activeSet ^ 1;
            if (Thread.interrupted()) throw new ComputationFailedException("Interrupted!");
            for (DuctNode<R> node : dirtySet[activeSet]) {
                Optional<R> result = updateNode(node);
                //noinspection OptionalAssignedToNull
                if (result != null) {
                    if (result.isPresent()) {
                        return result.get();
                    } else {
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

}
