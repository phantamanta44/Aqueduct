package xyz.phanta.aqueduct.engine;

import xyz.phanta.aqueduct.graph.edge.DuctEdge;
import xyz.phanta.aqueduct.graph.edge.EdgeMode;
import xyz.phanta.aqueduct.graph.node.DuctNode;
import xyz.phanta.aqueduct.graph.node.NodeAttribute;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDuctEngine<R> implements IDuctEngine<R> {

    protected final PolicyDefinitions policies;

    private boolean computed = false;

    public AbstractDuctEngine(PolicyDefinitions policies) {
        this.policies = policies;
    }

    protected void beforeCompute() {
        if (computed) throw new IllegalStateException("Engine has already performed computation!");
    }

    @Nullable
    protected Optional<R> updateNode(DuctNode<R> node) {
        if (node.checkInputs()) {
            if (node.hasAttrib(NodeAttribute.GREEDY)) {
                do {
                    Optional<R> result = node.getExecutor().execute(node.dequeueInputs(), node.getOutputs());
                    if (result.isPresent()) return result;
                } while (node.checkInputs());
                return Optional.empty();
            } else {
                return node.getExecutor().execute(node.dequeueInputs(), node.getOutputs());
            }
        }
        //noinspection OptionalAssignedToNull
        return null;
    }

    protected <T> boolean updateEdge(DuctEdge<T> edge) {
        EdgeMode mode = edge.getEdgeMode();
        if (mode == EdgeMode.TRANSFER_ONE) {
            if (!edge.getSource().isDataAvailable()) return false;
            edge.getDestination().enqueue(edge.getSource().dequeue());
        } else if (mode == EdgeMode.TRANSFER_ALL) {
            List<T> data = edge.getSource().dequeueAll();
            if (data.isEmpty()) return false;
            edge.getDestination().enqueue(data);
        } else {
            List<T> data = edge.getSource().dequeueSome(mode.getCount());
            if (data.isEmpty()) return false;
            edge.getDestination().enqueue(data);
        }
        return true;
    }

}
