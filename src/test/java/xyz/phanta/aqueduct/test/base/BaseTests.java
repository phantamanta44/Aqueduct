package xyz.phanta.aqueduct.test.base;

import org.junit.Assert;
import org.junit.Test;
import xyz.phanta.aqueduct.engine.IDuctEngine;
import xyz.phanta.aqueduct.graph.IGraphBuilder;
import xyz.phanta.aqueduct.graph.node.INodeConfiguration;
import xyz.phanta.aqueduct.graph.socket.IncomingSocket;
import xyz.phanta.aqueduct.graph.socket.OutgoingSocket;
import xyz.phanta.aqueduct.predef.source.SourceNodes;

import java.util.Optional;

public abstract class BaseTests {

    protected abstract <R> IGraphBuilder<R, ? extends IDuctEngine<R>> getGraphBuilder();

    private static final String[] LOREM_IPSUM = {
            "lorem", "ipsum", "dolor", "sit", "amet", "consectetur", "adipiscing", "elit", "sed", "do", "eiusmod",
            "tempor", "incididunt", "ut", "labore", "et", "dolore", "magna", "aliqua"
    };

    @Test(timeout = 1000L)
    public void testLoremIpsum() {
        IGraphBuilder<String[], ? extends IDuctEngine<String[]>> builder = getGraphBuilder();

        // generates lorem ipsum
        INodeConfiguration<String[]> gen = builder.createNode(SourceNodes.ofValues(LOREM_IPSUM));
        OutgoingSocket<String, String[]> genOut = gen.openSocketOut(String.class);

        // consumes and collects strings
        INodeConfiguration<String[]> snk = builder.createNode((params, outputs) -> Optional.of(params.get(0).stream()
                .map(s -> (String)s).toArray(String[]::new)));
        IncomingSocket<String, String[]> snkIn = snk.openSocketIn(String.class, LOREM_IPSUM.length);

        builder.createEdge(genOut, snkIn);

        Assert.assertArrayEquals("Lorem ipsum", LOREM_IPSUM, builder.finish().computeBlocking());
    }

    @Test(timeout = 1000L)
    public void testIntSequence() {
        IGraphBuilder<Integer, ? extends IDuctEngine<Integer>> builder = getGraphBuilder();

        // generates first 100 nonnegative ints
        INodeConfiguration<Integer> gen = builder.createNode(SourceNodes.intsLimited(0, 100));
        OutgoingSocket<Integer, Integer> genOut = gen.openSocketOut(Integer.class);

        // consumes and collects ints
        INodeConfiguration<Integer> snk = builder.createNode((params, outputs) -> Optional.of(params.get(0).stream()
                .mapToInt(i -> (Integer)i).sum()));
        IncomingSocket<Integer, Integer> snkIn = snk.openSocketIn(Integer.class, 100);

        builder.createEdge(genOut, snkIn);

        Assert.assertEquals("Sum of first 100 nonnegative ints", 4950, builder.finish().computeBlocking().intValue());
    }

    // TODO finish tests

}
