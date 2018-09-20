package xyz.phanta.aqueduct.graph.builder;

import xyz.phanta.aqueduct.graph.socket.IncomingSocket;
import xyz.phanta.aqueduct.graph.socket.OutgoingSocket;

public interface IConnectable {

    <T> OutgoingSocket<T> openSocketOut(Class<T> dataType);

    <T> IncomingSocket<T> openSocketIn(Class<T> dataType, int count);

    default <T> IncomingSocket<T> openSocketIn(Class<T> dataType) {
        return openSocketIn(dataType, 1);
    }

}
