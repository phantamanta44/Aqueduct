package xyz.phanta.aqueduct.graph.builder;

import xyz.phanta.aqueduct.engine.IDuctEngine;
import xyz.phanta.aqueduct.execution.INodeExecutor;
import xyz.phanta.aqueduct.graph.edge.IEdgeConfiguration;
import xyz.phanta.aqueduct.graph.node.INodeConfiguration;
import xyz.phanta.aqueduct.graph.socket.IncomingSocket;
import xyz.phanta.aqueduct.graph.socket.OutgoingSocket;

import java.util.function.Consumer;

public interface IGraphBuilder<R> {

    INodeConfiguration createNode(INodeExecutor<R> executor);

    <T> IEdgeConfiguration createEdge(OutgoingSocket<T> source, IncomingSocket<T> destination);

    IChainBuilder<R> createChain(INodeExecutor<R> first);

    IChainBuilder<R> createChain(INodeExecutor<R> first, Consumer<INodeConfiguration> configurator);

    IDuctEngine<R> finish();

}
