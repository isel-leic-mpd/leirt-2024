package isel.leirt.mpd.functional;

import java.util.function.Function;

@FunctionalInterface
public interface Comparator<T> {
    int compare(T t1, T t2);
    
    static <T,U extends Comparable<U>> Comparator<T> comparing(Function<T,U> key) {
        return (t1, t2) -> {
            var k1 = key.apply(t1);
            var k2 = key.apply(t2);
            return k1.compareTo(k2);
        };
    }
    
    public static <T, U> Comparator<T> comparing(
        Function<T, U> key,
        Comparator<U> keyComparator)
    {
        return (t1, t2) -> {
            var k1 = key.apply(t1);
            var k2 = key.apply(t2);
            return keyComparator.compare(k1, k2);
        };
        
    }
    
    default <U extends Comparable<U>> Comparator<T> thenComparing(Function<T,U> key) {
         return (t1, t2) -> {
             int res = compare(t1, t2);
             if (res != 0) return res;
             U k1 = key.apply(t1);
             U k2 = key.apply(t2);
             return k1.compareTo(k2);
         };
    }
   
    
    /**
     * Returns a comparator that imposes the reverse ordering of this comparator.
     */
    default Comparator<T> reversed() {
        return (t1, t2) -> compare(t2, t1);
    }
    
}
