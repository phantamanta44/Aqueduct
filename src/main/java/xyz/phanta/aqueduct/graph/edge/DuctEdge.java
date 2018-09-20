package xyz.phanta.aqueduct.graph.edge;

import xyz.phanta.aqueduct.engine.PolicyDefinitions;
import xyz.phanta.aqueduct.graph.socket.IncomingSocket;
import xyz.phanta.aqueduct.graph.socket.OutgoingSocket;

public class DuctEdge<T> implements IEdgeConfiguration {

    private final OutgoingSocket<T> source;
    private final IncomingSocket<T> destination;

    private EdgeMode mode;

    public DuctEdge(OutgoingSocket<T> source, IncomingSocket<T> destination, PolicyDefinitions policies) {
        if (!destination.getDataType().isAssignableFrom(source.getDataType())) {
            throw new ClassCastException(String.format("%s socket cannot accept %s!",
                    destination.getDataType(), source.getDataType()));
        }
        this.source = source;
        this.destination = destination;
        this.mode = policies.getDefaultEdgeMode();
    }

    @Override
    public IEdgeConfiguration withEdgeMode(EdgeMode mode) {
        this.mode = mode;
        return this;
    }

    public OutgoingSocket<T> getSource() {
        return source;
    }

    public IncomingSocket<T> getDestination() {
        return destination;
    }

    public EdgeMode getEdgeMode() {
        return mode;
    }

    public boolean validateSource(OutgoingSocket<T> source) {
        return source == this.source;
    }

    public boolean validateDestination(IncomingSocket<T> destination) {
        return destination == this.destination;
    }



}
