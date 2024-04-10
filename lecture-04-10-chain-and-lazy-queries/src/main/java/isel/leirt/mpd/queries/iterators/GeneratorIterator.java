package isel.leirt.mpd.queries.iterators;

import java.util.Iterator;
import java.util.function.Supplier;

public class GeneratorIterator<T> implements Iterator<T> {
    
    private final Supplier<T> supplier;
    
    public GeneratorIterator(Supplier<T> supplier) {
        this.supplier = supplier;
    }
    @Override
    public boolean hasNext() {
        return true;
    }
    
    @Override
    public T next() {
        return supplier.get();
    }
}
