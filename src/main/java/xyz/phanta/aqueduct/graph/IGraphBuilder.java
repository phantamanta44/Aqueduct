package xyz.phanta.aqueduct.graph;

import xyz.phanta.aqueduct.engine.IDuctEngine;
import xyz.phanta.aqueduct.graph.edge.IEdgeConfiguration;
import xyz.phanta.aqueduct.graph.node.INodeConfiguration;
import xyz.phanta.aqueduct.execution.INodeExecutor;
import xyz.phanta.aqueduct.graph.socket.IncomingSocket;
import xyz.phanta.aqueduct.graph.socket.OutgoingSocket;

public interface IGraphBuilder<R> {

    INodeConfiguration createNode(INodeExecutor<R> executor);

    <T> IEdgeConfiguration createEdge(OutgoingSocket<T> source, IncomingSocket<T> destination);

    IDuctEngine<R> finish();

}
