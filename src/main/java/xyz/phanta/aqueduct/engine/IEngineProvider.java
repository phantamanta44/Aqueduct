package xyz.phanta.aqueduct.engine;

import xyz.phanta.aqueduct.graph.IGraphBuilder;

public interface IEngineProvider {

    <R> IGraphBuilder<R, ? extends IDuctEngine<R>> beginBuilder();

}
