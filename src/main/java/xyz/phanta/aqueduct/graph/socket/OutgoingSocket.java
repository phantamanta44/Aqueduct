package xyz.phanta.aqueduct.graph.socket;

import xyz.phanta.aqueduct.graph.edge.DuctEdge;
import xyz.phanta.aqueduct.graph.node.DuctNode;

public class OutgoingSocket<T, R> extends DuctSocket<T, R> {

    public OutgoingSocket(Class<T> dataType, DuctNode<R> owner) {
        super(dataType, owner);
    }

    @Override
    boolean isEdgeValid(DuctEdge<T, R> edge) {
        return edge.validateSource(this);
    }

}
