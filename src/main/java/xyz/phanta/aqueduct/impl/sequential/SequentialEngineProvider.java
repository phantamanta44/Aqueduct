package xyz.phanta.aqueduct.impl.sequential;

import xyz.phanta.aqueduct.engine.IEngineProvider;
import xyz.phanta.aqueduct.engine.PolicyDefinitions;
import xyz.phanta.aqueduct.graph.DuctGraph;
import xyz.phanta.aqueduct.graph.IGraphBuilder;

public class SequentialEngineProvider implements IEngineProvider {

    public static final SequentialEngineProvider INSTANCE = new SequentialEngineProvider();

    @Override
    public <R> IGraphBuilder<R, SequentialEngine<R>> beginBuilder() {
        PolicyDefinitions policies = new PolicyDefinitions();
        return new DuctGraph<>(policies, graph -> new SequentialEngine<>(graph, policies));
    }

}
