package xyz.phanta.aqueduct.graph.node;

import xyz.phanta.aqueduct.engine.PolicyDefinitions;
import xyz.phanta.aqueduct.graph.socket.IncomingSocket;
import xyz.phanta.aqueduct.graph.socket.OutgoingSocket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DuctNode<R> implements INodeConfiguration {

    private final INodeExecutor<R> executor;
    private final List<IncomingSocket<?>> inputs = new ArrayList<>();
    private final List<OutgoingSocket<?>> outputs = new ArrayList<>();

    private Set<NodeAttribute> attribs;

    public DuctNode(INodeExecutor<R> executor, PolicyDefinitions policies) {
        this.executor = executor;
        this.attribs = policies.getDefaultNodeAttribs();
    }

    @Override
    public INodeConfiguration withAttribs(Set<NodeAttribute> attribs) {
        this.attribs = Collections.unmodifiableSet(attribs);
        return this;
    }

    @Override
    public <T> OutgoingSocket<T> openSocketOut(Class<T> dataType) {
        OutgoingSocket<T> socket = new OutgoingSocket<>(dataType, this);
        outputs.add(socket);
        return socket;
    }

    @Override
    public <T> IncomingSocket<T> openSocketIn(Class<T> dataType, int count) {
        IncomingSocket<T> socket = new IncomingSocket<>(dataType, this, count);
        inputs.add(socket);
        return socket;
    }

    public INodeExecutor<R> getExecutor() {
        return executor;
    }

    public List<IncomingSocket<?>> getInputs() {
        return inputs;
    }

    public List<OutgoingSocket<?>> getOutputs() {
        return outputs;
    }

    public boolean checkInputs() {
        return inputs.stream().allMatch(IncomingSocket::isFulfilled);
    }

    public List<List<?>> dequeueInputs() {
        return inputs.stream().map(IncomingSocket::dequeueParamData).collect(Collectors.toList());
    }

    public boolean hasAttrib(NodeAttribute attrib) {
        return attribs.contains(attrib);
    }

}
