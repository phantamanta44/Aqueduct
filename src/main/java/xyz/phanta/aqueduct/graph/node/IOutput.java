package xyz.phanta.aqueduct.graph.node;

import java.util.List;

public interface IOutput {

    void write(Object datum);

    void write(List<Object> data);

}
