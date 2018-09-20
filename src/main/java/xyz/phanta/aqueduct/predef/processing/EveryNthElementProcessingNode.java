package xyz.phanta.aqueduct.predef.processing;

import xyz.phanta.aqueduct.execution.INodeExecutor;
import xyz.phanta.aqueduct.execution.Outputs;
import xyz.phanta.aqueduct.execution.Parameters;

import java.util.Optional;

class EveryNthElementProcessingNode<R> implements INodeExecutor<R> {

    private final int index;

    private int counter = 0;

    EveryNthElementProcessingNode(int index) {
        this.index = index;
    }

    @Override
    public Optional<R> execute(Parameters params, Outputs outputs) {
        if (++counter == index) {
            counter = 0;
            outputs.put(params.val());
        }
        return Optional.empty();
    }

}
