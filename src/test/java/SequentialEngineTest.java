import org.junit.Assert;
import org.junit.Test;
import xyz.phanta.aqueduct.graph.IGraphBuilder;
import xyz.phanta.aqueduct.graph.node.INodeConfiguration;
import xyz.phanta.aqueduct.graph.socket.IncomingSocket;
import xyz.phanta.aqueduct.graph.socket.OutgoingSocket;
import xyz.phanta.aqueduct.impl.sequential.SequentialEngine;
import xyz.phanta.aqueduct.impl.sequential.SequentialEngineProvider;
import xyz.phanta.aqueduct.predef.source.SourceNodes;

import java.util.Optional;

// TODO finish tests
public class SequentialEngineTest {

    @Test(timeout = 1500L)
    public void testIntSequence() {
        IGraphBuilder<Integer, SequentialEngine<Integer>> builder
                = SequentialEngineProvider.INSTANCE.beginBuilder();

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

}
