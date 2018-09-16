package xyz.phanta.aqueduct.graph.node;

import xyz.phanta.aqueduct.graph.socket.IncomingSocket;
import xyz.phanta.aqueduct.graph.socket.OutgoingSocket;

import java.util.Set;

public interface INodeConfiguration {

    INodeConfiguration withAttribs(Set<NodeAttribute> attribs);

    <T> OutgoingSocket<T> openSocketOut(Class<T> dataType);

    <T> IncomingSocket<T> openSocketIn(Class<T> dataType, int count);

    default <T> IncomingSocket<T> openSocketIn(Class<T> dataType) {
        return openSocketIn(dataType, 1);
    }

}
