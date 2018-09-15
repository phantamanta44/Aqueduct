package xyz.phanta.aqueduct.graph.edge;

import xyz.phanta.aqueduct.engine.PolicyDefinitions;
import xyz.phanta.aqueduct.graph.socket.IncomingSocket;
import xyz.phanta.aqueduct.graph.socket.OutgoingSocket;

public class DuctEdge<T, R> implements IEdgeConfiguration {

    private final OutgoingSocket<T, R> source;
    private final IncomingSocket<T, R> destination;

    private EdgeMode mode;

    public DuctEdge(OutgoingSocket<T, R> source, IncomingSocket<T, R> destination, PolicyDefinitions policies) {
        this.source = source;
        this.destination = destination;
        this.mode = policies.getDefaultEdgeMode();
    }

    @Override
    public IEdgeConfiguration withEdgeMode(EdgeMode mode) {
        this.mode = mode;
        return this;
    }

    public OutgoingSocket<T, R> getSource() {
        return source;
    }

    public IncomingSocket<T, R> getDestination() {
        return destination;
    }

    public EdgeMode getEdgeMode() {
        return mode;
    }

    public boolean validateSource(OutgoingSocket<T, R> source) {
        return source == this.source;
    }

    public boolean validateDestination(IncomingSocket<T, R> destination) {
        return destination == this.destination;
    }



}
