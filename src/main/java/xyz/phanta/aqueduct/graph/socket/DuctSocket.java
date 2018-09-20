package xyz.phanta.aqueduct.graph.socket;

import xyz.phanta.aqueduct.ComputationFailedException;
import xyz.phanta.aqueduct.graph.edge.DuctEdge;
import xyz.phanta.aqueduct.graph.node.DuctNode;
import xyz.phanta.aqueduct.graph.node.IOutput;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class DuctSocket<T> implements IOutput {

    private final Class<T> dataType;
    private final DuctNode<?> owner;
    private final Set<DuctEdge<T>> edges = new HashSet<>();
    private final LinkedList<T> buffer = new LinkedList<>();
    private final ReentrantReadWriteLock bufferLock = new ReentrantReadWriteLock();

    DuctSocket(Class<T> dataType, DuctNode<?> owner) {
        this.dataType = dataType;
        this.owner = owner;
    }

    public Class<T> getDataType() {
        return dataType;
    }

    public DuctNode<?> getOwner() {
        return owner;
    }

    public Set<DuctEdge<T>> getEdges() {
        return Collections.unmodifiableSet(edges);
    }

    public void attachEdge(DuctEdge<T> edge) {
        if (isEdgeValid(edge)) {
            edges.add(edge);
        } else {
            throw new IllegalArgumentException("Unowned edge!");
        }
    }

    abstract boolean isEdgeValid(DuctEdge<T> edge);

    public boolean isDataAvailable() {
        try {
            bufferLock.writeLock().lockInterruptibly();
            try {
                return !buffer.isEmpty();
            } finally {
                bufferLock.writeLock().unlock();
            }
        } catch (InterruptedException e) {
            throw new ComputationFailedException(e);
        }
    }

    public int getAvailableDataCount() {
        try {
            bufferLock.writeLock().lockInterruptibly();
            try {
                return buffer.size();
            } finally {
                bufferLock.writeLock().unlock();
            }
        } catch (InterruptedException e) {
            throw new ComputationFailedException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(Object datum) {
        if (!dataType.isInstance(datum)) {
            throw new ClassCastException(String.format("%s is not a %s!", datum.getClass(), dataType));
        }
        enqueue((T)datum);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void writeMany(List<?> data) {
        for (Object datum : data) {
            if (!dataType.isInstance(datum)) {
                throw new ClassCastException(String.format("%s is not a %s!", datum.getClass(), dataType));
            }
        }
        enqueue((List<T>)data);
    }

    public void enqueue(T datum) {
        try {
            bufferLock.writeLock().lockInterruptibly();
            try {
                buffer.add(datum);
            } finally {
                bufferLock.writeLock().unlock();
            }
        } catch (InterruptedException e) {
            throw new ComputationFailedException(e);
        }
    }

    public void enqueue(List<T> data) {
        try {
            bufferLock.writeLock().lockInterruptibly();
            try {
               buffer.addAll(data);
            } finally {
                bufferLock.writeLock().unlock();
            }
        } catch (InterruptedException e) {
            throw new ComputationFailedException(e);
        }
    }

    public T dequeue() {
        try {
            bufferLock.writeLock().lockInterruptibly();
            try {
                return Objects.requireNonNull(buffer.poll());
            } finally {
                bufferLock.writeLock().unlock();
            }
        } catch (InterruptedException e) {
            throw new ComputationFailedException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> dequeueAll() {
        try {
            bufferLock.writeLock().lockInterruptibly();
            try {
                List<T> result = (List<T>)buffer.clone();
                buffer.clear();
                return result;
            } finally {
                bufferLock.writeLock().unlock();
            }
        } catch (InterruptedException e) {
            throw new ComputationFailedException(e);
        }
    }

    public List<T> dequeueSome(int count) {
        try {
            bufferLock.writeLock().lockInterruptibly();
            try {
                if (count == 0) return Collections.emptyList();
                if (count >= buffer.size()) return dequeueAll();
                List<T> segment = buffer.subList(0, count);
                List<T> result = new LinkedList<>(segment);
                segment.clear();
                return result;
            } finally {
                bufferLock.writeLock().unlock();
            }
        } catch (InterruptedException e) {
            throw new ComputationFailedException(e);
        }
    }

}
