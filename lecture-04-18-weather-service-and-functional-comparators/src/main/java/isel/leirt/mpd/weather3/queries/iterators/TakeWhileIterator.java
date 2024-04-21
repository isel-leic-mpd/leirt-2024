package isel.leirt.mpd.weather3.queries.iterators;

import java.util.Iterator;
import java.util.function.Predicate;

public class TakeWhileIterator<T> implements Iterator<T> {
    
    private final Predicate<T> pred;
    private final Iterator<T> srcIt;
    
    private T curr = null;
    private boolean done;
    
    public TakeWhileIterator(Iterable<T> src, Predicate<T> pred) {
        this.srcIt = src.iterator();
        this.pred = pred;
    }
    
    @Override
    public boolean hasNext() {
        if (done) return false;
        if (curr != null) return true;
        if (!srcIt.hasNext()) {
            done = true;
            return false;
        }
        var t = srcIt.next();
        if (!pred.test(t)) {
            done = true;
            return false;
        }
        curr = t;
        return true;
    }
    
    @Override
    public T next() {
        if (!hasNext()) throw new IllegalStateException();
        var c = curr;
        curr = null;
        
        return c;
    }
}
