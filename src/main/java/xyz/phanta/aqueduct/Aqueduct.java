package xyz.phanta.aqueduct;

import xyz.phanta.aqueduct.engine.IEngineProvider;
import xyz.phanta.aqueduct.impl.parallel.ParallelEngineProvider;
import xyz.phanta.aqueduct.impl.sequential.SequentialEngineProvider;

import java.util.NoSuchElementException;

public class Aqueduct {

    public static IEngineProvider getEngine(String name) {
        switch (name.toLowerCase()) {
            case "parallel":
                return ParallelEngineProvider.INSTANCE;
            case "sequential":
                return SequentialEngineProvider.INSTANCE;
        }
        throw new NoSuchElementException("Could not find engine: " + name);
    }

}
