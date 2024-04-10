package isel.leirt.mpd.queries.iterators;

import isel.leirt.mpd.queries.PipeIterable;

import java.util.Iterator;
import java.util.function.Predicate;

public class FilterIterator<T> implements Iterator<T> {
    
    private final Iterator<T> srcIt;
    private final Predicate<T> pred;
    
    private T value;
    
    public FilterIterator(PipeIterable<T> src, Predicate<T> pred) {
        this.srcIt = src.iterator();
        this.pred = pred;
        this.value = null;
    }
    @Override
    public boolean hasNext() {
        if (value != null) return true;
        while(srcIt.hasNext())  {
            var e = srcIt.next();
            if (pred.test(e)) {
                value = e;
                return true;
            }
        }
        return false;
    }
    
    @Override
    public T next() {
        if (!hasNext()) throw new IllegalStateException();
        var v = value;
        value = null;
        return v;
    }
}
