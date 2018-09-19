package xyz.phanta.aqueduct.execution;

import java.util.List;

@SuppressWarnings("unchecked")
public class Parameters {

    private final List<List<?>> params;

    public Parameters(List<List<?>> params) {
        this.params = params;
    }

    public <T> List<T> at(int index) {
        return (List<T>)params.get(index);
    }

    public <T> T valAt(int index) {
        return this.<T>at(index).get(0);
    }

    public <T> List<T> vals() {
        return at(0);
    }

    public <T> T val() {
        return valAt(0);
    }

}
