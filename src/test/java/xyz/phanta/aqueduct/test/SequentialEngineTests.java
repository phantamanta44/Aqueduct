package xyz.phanta.aqueduct.test;

import xyz.phanta.aqueduct.graph.IGraphBuilder;
import xyz.phanta.aqueduct.impl.sequential.SequentialEngineProvider;
import xyz.phanta.aqueduct.test.base.BaseTests;

public class SequentialEngineTests extends BaseTests {

    @Override
    protected <R> IGraphBuilder<R> getGraphBuilder() {
        return SequentialEngineProvider.INSTANCE.beginBuilder();
    }

}
