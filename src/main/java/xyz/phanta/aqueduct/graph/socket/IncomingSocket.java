package xyz.phanta.aqueduct.graph.socket;

import xyz.phanta.aqueduct.graph.edge.DuctEdge;
import xyz.phanta.aqueduct.graph.node.DuctNode;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class IncomingSocket<T> extends DuctSocket<T> {

    private final int count;

    private boolean notRequired = false;
    @Nullable
    private Supplier<List<T>> boundSupplier = null;

    public IncomingSocket(Class<T> dataType, DuctNode<?> owner, int count) {
        super(dataType, owner);
        this.count = count;
    }

    public IncomingSocket notReq() {
        notRequired = true;
        return this;
    }

    public void bindVal(T value) {
        if (!getEdges().isEmpty()) throw new UnsupportedOperationException("Socket is bound to an edge!");
        List<T> values = Collections.singletonList(value);
        this.boundSupplier = () -> values;
    }

    public void bindVals(List<T> values) {
        if (!getEdges().isEmpty()) throw new UnsupportedOperationException("Socket is bound to an edge!");
        this.boundSupplier = () -> values;
    }

    public void bind(Supplier<List<T>> supplier) {
        if (!getEdges().isEmpty()) throw new UnsupportedOperationException("Socket is bound to an edge!");
        this.boundSupplier = supplier;
    }

    public boolean isFulfilled() {
        return notRequired || boundSupplier != null || getAvailableDataCount() >= count;
    }

    public List<T> dequeueParamData() {
        return boundSupplier != null ? boundSupplier.get() : dequeueSome(count);
    }

    @Override
    public void attachEdge(DuctEdge<T> edge) {
        if (boundSupplier != null) throw new UnsupportedOperationException("Socket is bound to a supplier!");
        super.attachEdge(edge);
    }

    @Override
    boolean isEdgeValid(DuctEdge<T> edge) {
        return edge.validateDestination(this);
    }

}
