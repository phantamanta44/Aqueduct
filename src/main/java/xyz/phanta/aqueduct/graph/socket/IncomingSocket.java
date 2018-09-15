package xyz.phanta.aqueduct.graph.socket;

import xyz.phanta.aqueduct.graph.edge.DuctEdge;
import xyz.phanta.aqueduct.graph.node.DuctNode;

import javax.annotation.Nonnegative;
import java.util.List;

public class IncomingSocket<T, R> extends DuctSocket<T, R> {

    private final int count;

    public IncomingSocket(Class<T> dataType, DuctNode<R> owner, @Nonnegative int count) {
        super(dataType, owner);
        this.count = count;
    }

    public boolean isFulfilled() {
        return getAvailableDataCount() >= count;
    }

    public List<T> dequeueParamData() {
        return dequeueSome(count);
    }

    @Override
    boolean isEdgeValid(DuctEdge<T, R> edge) {
        return edge.validateDestination(this);
    }

}
