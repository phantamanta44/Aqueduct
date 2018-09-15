package xyz.phanta.aqueduct.graph.edge;

import javax.annotation.Nonnegative;

public abstract class EdgeMode {
    
    public static final EdgeMode TRANSFER_ONE = new EdgeModeTransferOne();
    public static final EdgeMode TRANSFER_ALL = new EdgeModeTransferAll();

    public static EdgeMode transferSome(@Nonnegative int count) {
        return new EdgeModeTransferSome(count);
    }

    private EdgeMode() {
        // NO-OP
    }

    @Nonnegative
    public abstract int getCount();
    
    private static class EdgeModeTransferOne extends EdgeMode {

        @Nonnegative
        @Override
        public int getCount() {
            return 1;
        }

    }
    
    private static class EdgeModeTransferSome extends EdgeMode {

        @Nonnegative
        private final int count;
        
        EdgeModeTransferSome(@Nonnegative int count) {
            this.count = count;
        }

        @Nonnegative
        @Override
        public int getCount() {
            return count;
        }

    }

    private static class EdgeModeTransferAll extends EdgeMode {

        @Nonnegative
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

    }
    
}
