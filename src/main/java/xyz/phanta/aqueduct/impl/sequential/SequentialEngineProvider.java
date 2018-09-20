package xyz.phanta.aqueduct.impl.sequential;

import xyz.phanta.aqueduct.engine.IEngineProvider;
import xyz.phanta.aqueduct.engine.PolicyDefinitions;
import xyz.phanta.aqueduct.graph.DuctGraph;
import xyz.phanta.aqueduct.graph.builder.IGraphBuilder;

public class SequentialEngineProvider implements IEngineProvider {

    public static final SequentialEngineProvider INSTANCE = new SequentialEngineProvider();

    private SequentialEngineProvider() {
        // NO-OP
    }

    @Override
    public <R> IGraphBuilder<R> beginBuilder() {
        PolicyDefinitions policies = new PolicyDefinitions();
        return new DuctGraph<R, SequentialEngine<R>>(policies, graph -> new SequentialEngine<>(graph, policies));
    }

}
