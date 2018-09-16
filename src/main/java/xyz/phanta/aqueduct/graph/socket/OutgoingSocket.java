package xyz.phanta.aqueduct.graph.socket;

import xyz.phanta.aqueduct.graph.edge.DuctEdge;
import xyz.phanta.aqueduct.graph.node.DuctNode;

public class OutgoingSocket<T> extends DuctSocket<T> {

    public OutgoingSocket(Class<T> dataType, DuctNode<?> owner) {
        super(dataType, owner);
    }

    @Override
    boolean isEdgeValid(DuctEdge<T> edge) {
        return edge.validateSource(this);
    }

}
