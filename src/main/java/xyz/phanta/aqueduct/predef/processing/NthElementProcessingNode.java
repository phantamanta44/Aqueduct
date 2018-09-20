package xyz.phanta.aqueduct.predef.processing;

import xyz.phanta.aqueduct.execution.INodeExecutor;
import xyz.phanta.aqueduct.execution.Outputs;
import xyz.phanta.aqueduct.execution.Parameters;

import java.util.Optional;

public class NthElementProcessingNode<R> implements INodeExecutor<R> {

    private int index;

    public NthElementProcessingNode(int index) {
        this.index = index;
    }

    @Override
    public Optional<R> execute(Parameters params, Outputs outputs) {
        if (index > 0 && --index == 0) outputs.put(params.val());
        return Optional.empty();
    }

}
