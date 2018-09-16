package xyz.phanta.aqueduct.test.base;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import xyz.phanta.aqueduct.graph.IGraphBuilder;
import xyz.phanta.aqueduct.graph.node.INodeConfiguration;
import xyz.phanta.aqueduct.graph.socket.IncomingSocket;
import xyz.phanta.aqueduct.graph.socket.OutgoingSocket;
import xyz.phanta.aqueduct.predef.source.SourceNodes;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public abstract class BaseTests {

    protected abstract <R> IGraphBuilder<R> getGraphBuilder();

    private static final String[] LOREM_IPSUM = {
            "lorem", "ipsum", "dolor", "sit", "amet", "consectetur", "adipiscing", "elit", "sed", "do", "eiusmod",
            "tempor", "incididunt", "ut", "labore", "et", "dolore", "magna", "aliqua"
    };

    private void doWithTimeout(Executable task) {
        assertTimeout(Duration.ofSeconds(1), task);
    }

    @Test
    @DisplayName("Lorem ipusm")
    void testLoremIpsum() {
        doWithTimeout(() -> {
            IGraphBuilder<String[]> builder = getGraphBuilder();

            // generates lorem ipsum
            INodeConfiguration gen = builder.createNode(SourceNodes.ofValues(LOREM_IPSUM));
            OutgoingSocket<String> genOut = gen.openSocketOut(String.class);

            // consumes and collects strings to an array
            INodeConfiguration snk = builder.createNode((params, outputs) -> Optional.of(params.get(0).stream()
                    .map(s -> (String)s).toArray(String[]::new)));
            IncomingSocket<String> snkIn = snk.openSocketIn(String.class, LOREM_IPSUM.length);

            builder.createEdge(genOut, snkIn);

            assertArrayEquals(LOREM_IPSUM, builder.finish().computeBlocking());
        });
    }

    @Test
    @DisplayName("Int sequence summation")
    void testIntSequence() {
        doWithTimeout(() -> {
            IGraphBuilder<Integer> builder = getGraphBuilder();

            // generates first 100 nonnegative ints
            INodeConfiguration gen = builder.createNode(SourceNodes.intsLimited(0, 100));
            OutgoingSocket<Integer> genOut = gen.openSocketOut(Integer.class);

            // consumes and sums 100 ints
            INodeConfiguration snk = builder.createNode((params, outputs) -> Optional.of(params.get(0).stream()
                    .mapToInt(i -> (Integer)i).sum()));
            IncomingSocket<Integer> snkIn = snk.openSocketIn(Integer.class, 100);

            builder.createEdge(genOut, snkIn);

            assertEquals(4950, builder.finish().computeBlocking().intValue(), "Sum of first 100 nonnegative ints");
        });
    }

    @Test
    @DisplayName("Int sequence mapping")
    void testIntSeqMapping() {
        doWithTimeout(() -> {
            IGraphBuilder<Integer> builder = getGraphBuilder();

            // generates first 100 nonnegative ints
            INodeConfiguration gen = builder.createNode(SourceNodes.intsLimited(0, 100));
            OutgoingSocket<Integer> genOut = gen.openSocketOut(Integer.class);

            // processes ints by f(n) = n * 2 + 5
            INodeConfiguration map = builder.createNode((params, outputs) -> {
                outputs.get(0).write((Integer)params.get(0).get(0) * 2 + 5);
                return Optional.empty();
            });
            IncomingSocket<Integer> mapIn = map.openSocketIn(Integer.class);
            OutgoingSocket<Integer> mapOut = map.openSocketOut(Integer.class);

            // consumes and sums 100 ints
            INodeConfiguration snk = builder.createNode((params, outputs) -> Optional.of(params.get(0).stream()
                    .mapToInt(i -> (Integer)i).sum()));
            IncomingSocket<Integer> snkIn = snk.openSocketIn(Integer.class, 100);

            builder.createEdge(genOut, mapIn);
            builder.createEdge(mapOut, snkIn);

            assertEquals(10400, builder.finish().computeBlocking().intValue(), "Sum of first 100 n*2+5 ints");
        });
    }

    // TODO finish tests

}
