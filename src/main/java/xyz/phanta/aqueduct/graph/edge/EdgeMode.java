package xyz.phanta.aqueduct.graph.edge;

public abstract class EdgeMode {
    
    public static final EdgeMode TRANSFER_ONE = new EdgeModeTransferOne();
    public static final EdgeMode TRANSFER_ALL = new EdgeModeTransferAll();

    public static EdgeMode transferSome(int count) {
        return new EdgeModeTransferSome(count);
    }

    private EdgeMode() {
        // NO-OP
    }

    public abstract int getCount();
    
    private static class EdgeModeTransferOne extends EdgeMode {

        @Override
        public int getCount() {
            return 1;
        }

    }
    
    private static class EdgeModeTransferSome extends EdgeMode {

        private final int count;
        
        EdgeModeTransferSome(int count) {
            this.count = count;
        }

        @Override
        public int getCount() {
            return count;
        }

    }

    private static class EdgeModeTransferAll extends EdgeMode {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

    }
    
}
