package isel.leirt.mpd.weather2.queries.iterators;

import java.util.Iterator;

public class RangeIterator implements Iterator<Integer> {
    int curr;
    int max;
    
    public RangeIterator(int min, int max) {
        this.curr = min;
        this.max = max;
    }
    @Override
    public boolean hasNext() {
        return curr <= max;
    }
    
    @Override
    public Integer next() {
        if (!hasNext()) throw new IllegalStateException();
        return curr++;
    }
}
