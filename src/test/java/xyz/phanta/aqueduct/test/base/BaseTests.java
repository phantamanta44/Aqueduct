package xyz.phanta.aqueduct.test.base;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import xyz.phanta.aqueduct.graph.IGraphBuilder;
import xyz.phanta.aqueduct.graph.node.INodeConfiguration;
import xyz.phanta.aqueduct.graph.socket.IncomingSocket;
import xyz.phanta.aqueduct.graph.socket.OutgoingSocket;
import xyz.phanta.aqueduct.predef.processing.ProcessNodes;
import xyz.phanta.aqueduct.predef.source.SourceNodes;
import xyz.phanta.aqueduct.predef.terminal.TerminalNodes;

import java.time.Duration;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public abstract class BaseTests {

    protected abstract <R> IGraphBuilder<R> getGraphBuilder();

    private void doWithTimeout(Executable task) {
        assertTimeoutPreemptively(Duration.ofSeconds(1), task);
    }

    private static final String[] LOREM_IPSUM = {
            "lorem", "ipsum", "dolor", "sit", "amet", "consectetur", "adipiscing", "elit", "sed", "do", "eiusmod",
            "tempor", "incididunt", "ut", "labore", "et", "dolore", "magna", "aliqua"
    };

    @Test
    @DisplayName("Lorem ipusm")
    void testLoremIpsum() {
        doWithTimeout(() -> {
            IGraphBuilder<String[]> builder = getGraphBuilder();

            // generates lorem ipsum
            INodeConfiguration gen = builder.createNode(SourceNodes.ofValues(LOREM_IPSUM));
            OutgoingSocket<String> genOut = gen.openSocketOut(String.class);

            // consumes and collects strings to an array
            //noinspection SuspiciousToArrayCall
            INodeConfiguration snk = builder.createNode(TerminalNodes.mapMany(vals -> vals.toArray(new String[0])));
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
            INodeConfiguration snk = builder.createNode(TerminalNodes.mapRaw(p -> p.iStream().sum()));
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
            INodeConfiguration map = builder.createNode(ProcessNodes.mapRaw(p -> p.iVal() * 2 + 5));
            IncomingSocket<Integer> mapIn = map.openSocketIn(Integer.class);
            OutgoingSocket<Integer> mapOut = map.openSocketOut(Integer.class);

            // consumes and sums 100 ints
            INodeConfiguration snk = builder.createNode(TerminalNodes.mapRaw(p -> p.iStream().sum()));
            IncomingSocket<Integer> snkIn = snk.openSocketIn(Integer.class, 100);

            builder.createEdge(genOut, mapIn);
            builder.createEdge(mapOut, snkIn);

            assertEquals(10400, builder.finish().computeBlocking().intValue(), "Sum of first 100 n*2+5 ints");
        });
    }

    private static final int[] INT_TRIPLE_SUMS = IntStream.range(0, 34).map(i -> 9 * i + 3).toArray();

    @Test
    @DisplayName("Int triple accumulation")
    void testTripleAccumulation() {
        doWithTimeout(() -> {
            IGraphBuilder<int[]> builder = getGraphBuilder();

            // generates first 102 nonnegative ints
            INodeConfiguration gen = builder.createNode(SourceNodes.intsLimited(0, 102));
            OutgoingSocket<Integer> genOut = gen.openSocketOut(Integer.class);

            // accumulates and sums every 3 ints
            INodeConfiguration acc = builder.createNode(ProcessNodes.mapRaw(p -> p.iStream().sum()));
            IncomingSocket<Integer> accIn = acc.openSocketIn(Integer.class, 3);
            OutgoingSocket<Integer> accOut = acc.openSocketOut(Integer.class);

            // collects ints into an array
            INodeConfiguration snk = builder.createNode(TerminalNodes.mapRaw(p -> p.iStream().toArray()));
            IncomingSocket<Integer> snkIn = snk.openSocketIn(Integer.class, 34);

            builder.createEdge(genOut, accIn);
            builder.createEdge(accOut, snkIn);

            assertArrayEquals(INT_TRIPLE_SUMS, builder.finish().computeBlocking(), "Set of first 34 int triple sums");
        });
    }

    private static final int[] FIRST_50_ODDS = IntStream.range(0, 50).map(i -> i * 2 + 1).toArray();

    @Test
    @DisplayName("Stream filtering")
    void testFilter() {
        doWithTimeout(() -> {
            IGraphBuilder<int[]> builder = getGraphBuilder();

            // generates first 100 nonegative ints
            INodeConfiguration gen = builder.createNode(SourceNodes.intsLimited(0, 100));
            OutgoingSocket<Integer> genOut = gen.openSocketOut(Integer.class);

            // filters out even ints
            INodeConfiguration acc = builder.createNode(ProcessNodes.filterRaw(p -> p.iVal() % 2 == 1));
            IncomingSocket<Integer> accIn = acc.openSocketIn(Integer.class);
            OutgoingSocket<Integer> accOut = acc.openSocketOut(Integer.class);

            // collects ints into an array
            INodeConfiguration snk = builder.createNode(TerminalNodes.mapRaw(p -> p.iStream().toArray()));
            IncomingSocket<Integer> snkIn = snk.openSocketIn(Integer.class, 50);

            builder.createEdge(genOut, accIn);
            builder.createEdge(accOut, snkIn);

            assertArrayEquals(FIRST_50_ODDS, builder.finish().computeBlocking(), "Set of first 34 int triple sums");
        });
    }

    private static final int[] FIRST_100_INTS = IntStream.range(0, 100).toArray();

    @Test
    @DisplayName("Stream zipping")
    void testZip() {
        doWithTimeout(() -> {
            IGraphBuilder<int[]> builder = getGraphBuilder();

            // generates even integers < 100
            INodeConfiguration genEven = builder.createNode(SourceNodes.intsLimited(0, 100, 2));
            OutgoingSocket<Integer> genEvenOut = genEven.openSocketOut(Integer.class);

            // generates odd integers < 100
            INodeConfiguration genOdd = builder.createNode(SourceNodes.intsLimited(1, 100, 2));
            OutgoingSocket<Integer> genOddOut = genOdd.openSocketOut(Integer.class);

            // zips two incoming streams
            INodeConfiguration zip = builder.createNode(ProcessNodes.zip());
            IncomingSocket<Integer> zipInLeft = zip.openSocketIn(Integer.class);
            IncomingSocket<Integer> zipInRight = zip.openSocketIn(Integer.class);
            OutgoingSocket<Integer> zipOut = zip.openSocketOut(Integer.class);

            // collects ints into an array
            INodeConfiguration snk = builder.createNode(TerminalNodes.mapRaw(p -> p.iStream().toArray()));
            IncomingSocket<Integer> snkIn = snk.openSocketIn(Integer.class, 100);

            builder.createEdge(genEvenOut, zipInLeft);
            builder.createEdge(genOddOut, zipInRight);
            builder.createEdge(zipOut, snkIn);

            assertArrayEquals(FIRST_100_INTS, builder.finish().computeBlocking());
        });
    }

    // TODO even more tests

}
