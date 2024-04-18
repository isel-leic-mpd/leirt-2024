package isel.leirt.mpd.weather2.queries.iterators;

import java.util.Iterator;
import java.util.function.Function;

public class MapIterator<T,U> implements Iterator<U> {
    
    private final Function<T,U> mapper;
    private final Iterator<T> srcIt;
    
    public MapIterator(Iterable<T> src, Function<T,U> mapper) {
        this.mapper = mapper;
        srcIt = src.iterator();
    }
    @Override
    public boolean hasNext() {
        return srcIt.hasNext();
    }
    
    @Override
    public U next() {
        if (!hasNext()) throw new IllegalStateException();
        return mapper.apply(srcIt.next());
       
    }
}
