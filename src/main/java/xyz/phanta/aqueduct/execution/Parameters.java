package xyz.phanta.aqueduct.execution;

import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class Parameters {

    private final List<List<?>> params;

    public Parameters(List<List<?>> params) {
        this.params = params;
    }

    // param stream
    
    public <T> List<T> at(int index) {
        return (List<T>)params.get(index);
    }

    public <T> List<T> vals() {
        return at(0);
    }

    public <T> Stream<T> streamAt(int index) {
        return this.<T>at(index).stream();
    }
    
    public <T> Stream<T> stream() {
        return this.<T>vals().stream();
    }
    
    public IntStream iStreamAt(int index) {
        return this.<Integer>streamAt(index).mapToInt(i -> i);
    }
    
    public IntStream iStream() {
        return this.<Integer>stream().mapToInt(i -> i);
    }
    
    public LongStream lStreamAt(int index) {
        return this.<Long>streamAt(index).mapToLong(l -> l);
    }

    public LongStream lStream() {
        return this.<Long>stream().mapToLong(l -> l);
    }

    public DoubleStream dStreamAt(int index) {
        return this.<Double>streamAt(index).mapToDouble(d -> d);
    }

    public DoubleStream dStream() {
        return this.<Double>stream().mapToDouble(d -> d);
    }

    // param singles
    
    public <T> T valAt(int index) {
        return this.<T>at(index).get(0);
    }

    public <T> T val() {
        return valAt(0);
    }
    
    public int iValAt(int index) {
        return valAt(index);
    }
    
    public int iVal() {
        return val();
    }
    
    public long lValAt(int index) {
        return valAt(index);
    }
    
    public long lVal() {
        return val();
    }

    public double dValAt(int index) {
        return valAt(index);
    }
    
    public double dVal() {
        return val();
    }

    public boolean bValAt(int index) {
        return valAt(index);
    }
    
    public boolean bVal() {
        return val();
    }

}
