package isel.leirt.mpd.queries.iterators;

import java.util.Iterator;
import java.util.function.Function;

public class FlatmapIterator<T, U> implements Iterator<U> {
    
    private final Function<T, Iterable<U>> mapper;
    private final Iterator<T> srcIt;
    private Iterator<U> currIt;
    
    private U curr;
    
    public FlatmapIterator(Iterable<T> src, Function<T,Iterable<U>> mapper) {
        this.mapper = mapper;
        srcIt = src.iterator();
    }
    
    @Override
    public boolean hasNext() {
        if (curr != null) return true;
        while(currIt == null || !currIt.hasNext()) {
            if (!srcIt.hasNext()) return false;
            currIt = mapper.apply(srcIt.next()).iterator();
        }
        curr = currIt.next();
        return true;
    }
    
    @Override
    public U next() {
        if (!hasNext()) throw new IllegalStateException();
        var c = curr;
        curr = null;
        return c;
    }
}
