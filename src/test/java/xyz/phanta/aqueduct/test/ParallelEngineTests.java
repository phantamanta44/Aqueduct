package xyz.phanta.aqueduct.test;

import xyz.phanta.aqueduct.graph.IGraphBuilder;
import xyz.phanta.aqueduct.impl.parallel.ParallelEngineProvider;
import xyz.phanta.aqueduct.test.base.BaseTests;

public class ParallelEngineTests extends BaseTests {

    @Override
    protected <R> IGraphBuilder<R> getGraphBuilder() {
        return ParallelEngineProvider.INSTANCE.beginBuilder();
    }

}
