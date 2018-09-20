package xyz.phanta.aqueduct.graph;

import xyz.phanta.aqueduct.engine.IDuctEngine;
import xyz.phanta.aqueduct.engine.PolicyDefinitions;
import xyz.phanta.aqueduct.execution.INodeExecutor;
import xyz.phanta.aqueduct.graph.builder.IChainBuilder;
import xyz.phanta.aqueduct.graph.builder.IConnectable;
import xyz.phanta.aqueduct.graph.builder.IGraphBuilder;
import xyz.phanta.aqueduct.graph.edge.DuctEdge;
import xyz.phanta.aqueduct.graph.edge.IEdgeConfiguration;
import xyz.phanta.aqueduct.graph.node.DuctNode;
import xyz.phanta.aqueduct.graph.node.INodeConfiguration;
import xyz.phanta.aqueduct.graph.socket.IncomingSocket;
import xyz.phanta.aqueduct.graph.socket.OutgoingSocket;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.function.Consumer;
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
    public DuctNode<R> createNode(INodeExecutor<R> executor) {
        checkFinished();
        DuctNode<R> node = new DuctNode<>(executor, policies);
        executor.init(node);
        nodes.add(node);
        return node;
    }

    @Override
    public <T> IEdgeConfiguration createEdge(OutgoingSocket<T> source, IncomingSocket<T> destination) {
        checkFinished();
        //noinspection SuspiciousMethodCalls
        if (nodes.contains(source.getOwner()) && nodes.contains(destination.getOwner())) {
            DuctEdge<T> edge = new DuctEdge<>(source, destination, policies);
            source.attachEdge(edge);
            destination.attachEdge(edge);
            edges.add(edge);
            return edge;
        } else {
            throw new IllegalArgumentException("At least one node not owned by graph!");
        }
    }

    @Override
    public IChainBuilder<R> createChain(INodeExecutor<R> first) {
        return new NodeChain(first);
    }

    @Override
    public IChainBuilder<R> createChain(INodeExecutor<R> first, Consumer<INodeConfiguration> configurator) {
        return new NodeChain(first, configurator);
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

    private class NodeChain implements IChainBuilder<R>, IConnectable {

        private final DuctNode<R> head;

        private DuctNode<R> tail;

        public NodeChain(INodeExecutor<R> first) {
            this.head = this.tail = createNode(first);
        }

        public NodeChain(INodeExecutor<R> first, Consumer<INodeConfiguration> configurator) {
            this(first);
            configurator.accept(this.head);
        }

        @Override
        public IChainBuilder<R> then(INodeExecutor<R> executor) {
            DuctNode<R> node = createNode(executor);
            createEdge(tail.openSocketOut(Object.class), node.openSocketIn(Object.class));
            tail = node;
            return this;
        }

        @Override
        public IChainBuilder<R> then(INodeExecutor<R> executor, Consumer<INodeConfiguration> configurator) {
            DuctNode<R> node = createNode(executor);
            configurator.accept(node);
            createEdge(tail.openSocketOut(Object.class), node.openSocketIn(Object.class));
            tail = node;
            return this;
        }

        @Override
        public IChainBuilder<R> thenEdge(INodeExecutor<R> executor, Consumer<IEdgeConfiguration> configurator) {
            DuctNode<R> node = createNode(executor);
            configurator.accept(createEdge(tail.openSocketOut(Object.class), node.openSocketIn(Object.class)));
            tail = node;
            return this;
        }

        @Override
        public IChainBuilder<R> thenEdge(INodeExecutor<R> executor, Consumer<IEdgeConfiguration> edgeConfigurator, Consumer<INodeConfiguration> nodeConfigurator) {
            DuctNode<R> node = createNode(executor);
            nodeConfigurator.accept(node);
            edgeConfigurator.accept(createEdge(tail.openSocketOut(Object.class), node.openSocketIn(Object.class)));
            tail = node;
            return this;
        }

        @Override
        public IConnectable finish() {
            return this;
        }

        @Override
        public <T> OutgoingSocket<T> openSocketOut(Class<T> dataType) {
            return tail.openSocketOut(dataType);
        }

        @Override
        public <T> IncomingSocket<T> openSocketIn(Class<T> dataType, int count) {
            return head.openSocketIn(dataType, count);
        }

        @Override
        public <T> IncomingSocket<T> openSocketIn(Class<T> dataType) {
            return head.openSocketIn(dataType);
        }

    }

}
