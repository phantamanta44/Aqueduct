package xyz.phanta.aqueduct.engine;

import xyz.phanta.aqueduct.graph.builder.IGraphBuilder;

public interface IEngineProvider {

    <R> IGraphBuilder<R> beginBuilder();

}
