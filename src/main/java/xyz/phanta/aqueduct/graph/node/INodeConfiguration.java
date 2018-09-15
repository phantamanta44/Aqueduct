package xyz.phanta.aqueduct.graph.node;

import xyz.phanta.aqueduct.graph.socket.IncomingSocket;
import xyz.phanta.aqueduct.graph.socket.OutgoingSocket;

import java.util.Set;

public interface INodeConfiguration<R> {

    INodeConfiguration withAttribs(Set<NodeAttribute> attribs);

    <T> OutgoingSocket<T, R> openSocketOut(Class<T> dataType);

    <T> IncomingSocket<T, R> openSocketIn(Class<T> dataType, int count);

    default <T> IncomingSocket<T, R> openSocketIn(Class<T> dataType) {
        return openSocketIn(dataType, 1);
    }

}
