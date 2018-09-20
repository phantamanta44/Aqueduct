package xyz.phanta.aqueduct.predef.processing;

import xyz.phanta.aqueduct.execution.INodeExecutor;
import xyz.phanta.aqueduct.execution.Outputs;
import xyz.phanta.aqueduct.execution.Parameters;
import xyz.phanta.aqueduct.graph.node.IOutput;
import xyz.phanta.aqueduct.util.DistribStrategy;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class DistributingProcessingNode<R> implements INodeExecutor<R> {

    private final Distributor distrib;

    public DistributingProcessingNode(DistribStrategy strategy) {
        Distributor distrib;
        switch (strategy) {
            case ROUND_ROBIN:
                distrib = new RoundRobinDistributor();
                break;
            case RANDOM:
                distrib = new RandomDistributor();
                break;
            default:
                throw new NullPointerException("Null strategy");
        }
        this.distrib = distrib;
    }

    @Override
    public Optional<R> execute(Parameters params, Outputs outputs) {
        distrib.distribute(params.vals(), outputs.getBacking());
        return Optional.empty();
    }

    private abstract class Distributor {

        abstract void distribute(List<?> values, List<? extends IOutput> destinations);

    }

    private class RoundRobinDistributor extends Distributor {

        private int index = 0;

        @Override
        void distribute(List<?> values, List<? extends IOutput> destinations) {
            int destCount = destinations.size();
            for (Object value : values) {
                destinations.get(index).write(value);
                index = (index + 1) % destCount;
            }
        }

    }

    private class RandomDistributor extends Distributor {

        private final Random rand = new Random();

        @Override
        void distribute(List<?> values, List<? extends IOutput> destinations) {
            int destCount = destinations.size();
            for (Object value : values) {
                destinations.get(rand.nextInt(destCount)).write(value);
            }
        }

    }

}
