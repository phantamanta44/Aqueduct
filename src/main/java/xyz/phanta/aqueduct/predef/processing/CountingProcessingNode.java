package xyz.phanta.aqueduct.predef.processing;

import xyz.phanta.aqueduct.execution.INodeExecutor;
import xyz.phanta.aqueduct.execution.Outputs;
import xyz.phanta.aqueduct.execution.Parameters;

import java.util.Optional;

public class CountingProcessingNode<R> implements INodeExecutor<R> {

    private int counter = 0;

    @Override
    public Optional<R> execute(Parameters params, Outputs outputs) {
        outputs.put(++counter);
        return Optional.empty();
    }

}
