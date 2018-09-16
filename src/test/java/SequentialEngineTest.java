import org.junit.Test;
import xyz.phanta.aqueduct.graph.IGraphBuilder;
import xyz.phanta.aqueduct.graph.node.INodeConfiguration;
import xyz.phanta.aqueduct.graph.socket.IncomingSocket;
import xyz.phanta.aqueduct.graph.socket.OutgoingSocket;
import xyz.phanta.aqueduct.impl.sequential.SequentialEngine;
import xyz.phanta.aqueduct.impl.sequential.SequentialEngineProvider;
import xyz.phanta.aqueduct.predef.source.SourceNodes;
import xyz.phanta.aqueduct.predef.terminal.TerminalNodes;

import java.util.Arrays;
import java.util.Optional;

// TODO finish tests
public class SequentialEngineTest {

    @Test(timeout = 1500L)
    public void testIntSequence() {
        IGraphBuilder<int[], SequentialEngine<int[]>> builder
                = SequentialEngineProvider.INSTANCE.beginBuilder();

        // generates first 100 nonnegative ints
        INodeConfiguration<int[]> gen = builder.createNode(SourceNodes.intsLimited(0, 100));
        OutgoingSocket<Integer, int[]> genOut = gen.openSocketOut(Integer.class);

        // consumes and collects ints
        INodeConfiguration<int[]> snk = builder.createNode(TerminalNodes.limited(100,
                (params, outputs) -> {
                    System.out.println(params.get(0).get(0));
                    return Optional.empty();
                },
                (params, outputs) -> Optional.of(new int[] { 48783 })));
        IncomingSocket<Integer, int[]> snkIn = snk.openSocketIn(Integer.class);

        builder.createEdge(genOut, snkIn);

        System.out.println(Arrays.toString(builder.finish().computeBlocking()));
    }

    @Test(timeout = 1500L)
    public void testFibonacciSum() {

    }

}
