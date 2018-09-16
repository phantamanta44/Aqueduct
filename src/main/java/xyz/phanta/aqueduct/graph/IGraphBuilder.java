package xyz.phanta.aqueduct.graph;

import xyz.phanta.aqueduct.engine.IDuctEngine;
import xyz.phanta.aqueduct.graph.edge.IEdgeConfiguration;
import xyz.phanta.aqueduct.graph.node.INodeConfiguration;
import xyz.phanta.aqueduct.graph.node.INodeExecutor;
import xyz.phanta.aqueduct.graph.socket.IncomingSocket;
import xyz.phanta.aqueduct.graph.socket.OutgoingSocket;

public interface IGraphBuilder<R, E extends IDuctEngine<R>> {

    INodeConfiguration<R> createNode(INodeExecutor<R> executor);

    <T> IEdgeConfiguration createEdge(OutgoingSocket<T, R> source, IncomingSocket<T, R> destination);

    E finish();

}
