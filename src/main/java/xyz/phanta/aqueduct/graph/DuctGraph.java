package xyz.phanta.aqueduct.graph;

import xyz.phanta.aqueduct.engine.IDuctEngine;
import xyz.phanta.aqueduct.engine.PolicyDefinitions;
import xyz.phanta.aqueduct.graph.edge.DuctEdge;
import xyz.phanta.aqueduct.graph.edge.IEdgeConfiguration;
import xyz.phanta.aqueduct.graph.node.DuctNode;
import xyz.phanta.aqueduct.graph.node.INodeConfiguration;
import xyz.phanta.aqueduct.graph.node.INodeExecutor;
import xyz.phanta.aqueduct.graph.socket.IncomingSocket;
import xyz.phanta.aqueduct.graph.socket.OutgoingSocket;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.function.Function;

public class DuctGraph<R, E extends IDuctEngine<R>> implements IGraphBuilder<R> {

    private final PolicyDefinitions policies;
    private final Function<DuctGraph<R, E>, E> engineFactory;
    private final Collection<DuctNode<R>> nodes = new HashSet<>();
    private final Collection<DuctEdge<?>> edges = new HashSet<>();

    private boolean finished = false;

    public DuctGraph(PolicyDefinitions policies, Function<DuctGraph<R, E>, E> engineFactory) {
        this.policies = policies;
        this.engineFactory = engineFactory;
    }

    @Override
    public INodeConfiguration createNode(INodeExecutor<R> executor) {
        checkFinished();
        DuctNode<R> node = new DuctNode<>(executor, policies);
        nodes.add(node);
        return node;
    }

    @Override
    public <T> IEdgeConfiguration createEdge(OutgoingSocket<T> source, IncomingSocket<T> destination) {
        checkFinished();
        //noinspection SuspiciousMethodCalls
        if (nodes.contains(source.getOwner()) && nodes.contains(destination.getOwner())) {
            DuctEdge<T> edge = new DuctEdge<>(source, destination, policies);
            edges.add(edge);
            return edge;
        } else {
            throw new IllegalArgumentException("At least one node not owned by graph!");
        }
    }

    @Override
    public E finish() {
        checkFinished();
        finished = true;
        return engineFactory.apply(this);
    }

    private void checkFinished() {
        if (finished) throw new IllegalStateException("Graph is already finished!");
    }

    public boolean isFinished() {
        return finished;
    }

    public Collection<DuctNode<R>> getNodes() {
        return Collections.unmodifiableCollection(nodes);
    }

    public Collection<DuctEdge<?>> getEdges() {
        return Collections.unmodifiableCollection(edges);
    }

}
