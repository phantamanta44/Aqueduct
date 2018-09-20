package xyz.phanta.aqueduct.graph.builder;

import xyz.phanta.aqueduct.execution.INodeExecutor;
import xyz.phanta.aqueduct.graph.edge.IEdgeConfiguration;
import xyz.phanta.aqueduct.graph.node.INodeConfiguration;

import java.util.function.Consumer;

public interface IChainBuilder<R> {

    IChainBuilder<R> then(INodeExecutor<R> executor);

    IChainBuilder<R> then(INodeExecutor<R> executor, Consumer<INodeConfiguration> configurator);

    IChainBuilder<R> thenEdge(INodeExecutor<R> executor, Consumer<IEdgeConfiguration> configurator);

    IChainBuilder<R> thenEdge(INodeExecutor<R> executor, Consumer<IEdgeConfiguration> edgeConfigurator,
                              Consumer<INodeConfiguration> nodeConfigurator);

    IConnectable finish();

}
