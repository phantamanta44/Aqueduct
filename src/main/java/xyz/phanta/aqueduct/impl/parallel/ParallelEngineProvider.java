package xyz.phanta.aqueduct.impl.parallel;

import xyz.phanta.aqueduct.engine.IEngineProvider;
import xyz.phanta.aqueduct.engine.PolicyDefinitions;
import xyz.phanta.aqueduct.graph.DuctGraph;
import xyz.phanta.aqueduct.graph.IGraphBuilder;

public class ParallelEngineProvider implements IEngineProvider {

    public static final ParallelEngineProvider INSTANCE = new ParallelEngineProvider();

    @Override
    public <R> IGraphBuilder<R> beginBuilder() {
        PolicyDefinitions policies = new PolicyDefinitions();
        return new DuctGraph<R, ParallelEngine<R>>(policies, graph -> new ParallelEngine<>(graph, policies));
    }

}
