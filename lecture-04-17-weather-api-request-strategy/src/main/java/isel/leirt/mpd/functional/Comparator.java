package isel.leirt.mpd.functional;

import java.util.function.Function;

@FunctionalInterface
public interface Comparator<T> {
    int compare(T t1, T t2);
    
    static <T,U extends Comparable<U>> Comparator<T> comparing(Function<T,U> key) {
        //TODO
        return null;
    }
    
    default <U extends Comparable<U>> Comparator<T> thenComparing(Function<T,U> key) {
        //TODO
        return null;
    }
    
    public static <T, U> Comparator<T> comparing(
        Function<T, U> keyExtractor,
        Comparator<U> keyComparator)
    {
        //TODO
        return null;
        
    }
    
    /**
     * Returns a comparator that imposes the reverse ordering of this comparator.
     */
    default Comparator<T> reversed() {
       // TO IMPLEMENT
        return null;
    }
    
}
