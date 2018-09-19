package xyz.phanta.aqueduct.execution;

import xyz.phanta.aqueduct.graph.node.IOutput;

import java.util.List;

public class Outputs {

    private final List<? extends IOutput> outputs;

    public Outputs(List<? extends IOutput> outputs) {
        this.outputs = outputs;
    }

    public IOutput at(int index) {
        return outputs.get(index);
    }

    public void putMany(int index, List<Object> values) {
        at(index).writeMany(values);
    }

    public void putMany(List<Object> values) {
        putMany(0, values);
    }

    public void putAt(int index, Object value) {
        at(index).write(value);
    }

    public void put(Object value) {
        putAt(0, value);
    }

}
