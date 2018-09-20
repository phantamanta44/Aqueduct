package xyz.phanta.aqueduct.predef.processing;

import xyz.phanta.aqueduct.execution.INodeExecutor;
import xyz.phanta.aqueduct.execution.Outputs;
import xyz.phanta.aqueduct.execution.Parameters;

import java.util.List;
import java.util.Optional;

class LimitedProcessingNode<R> implements INodeExecutor<R> {

    private int count;

    LimitedProcessingNode(int count) {
        this.count = count;
    }

    @Override
    public Optional<R> execute(Parameters params, Outputs outputs) {
        if (count > 0) {
            --count;
            List<List<?>> rawParams = params.getBacking();
            for (int i = 0; i < rawParams.size(); i++) outputs.putMany(i, rawParams.get(i));
        }
        return Optional.empty();
    }

}
