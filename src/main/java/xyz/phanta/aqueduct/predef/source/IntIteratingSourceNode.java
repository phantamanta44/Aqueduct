package xyz.phanta.aqueduct.predef.source;

import xyz.phanta.aqueduct.graph.node.INonTerminalExecutor;
import xyz.phanta.aqueduct.graph.node.IOutput;

import javax.annotation.Nonnegative;
import java.util.List;

public class IntIteratingSourceNode<R> implements INonTerminalExecutor<R> {

    private final int limit;
    @Nonnegative
    private final int step;

    private int value;

    public IntIteratingSourceNode(int initial, int limit, @Nonnegative int step) {
        this.limit = limit;
        this.step = step;
        this.value = initial;
    }

    @Override
    public void executeNonTerminal(List<List<?>> params, List<? extends IOutput> outputs) {
        if (value < limit) {
            outputs.get(0).write(value);
            value += step;
        }
    }

}
