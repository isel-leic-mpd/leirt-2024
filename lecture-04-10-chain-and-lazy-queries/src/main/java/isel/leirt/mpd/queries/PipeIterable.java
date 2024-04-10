package isel.leirt.mpd.queries;

import isel.leirt.mpd.queries.iterators.FilterIterator;
import isel.leirt.mpd.queries.iterators.GeneratorIterator;
import isel.leirt.mpd.queries.iterators.MapIterator;
import isel.leirt.mpd.queries.iterators.RangeIterator;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface PipeIterable<T> extends Iterable<T> {
    
    // factory operations
    
    static <T> PipeIterable<T> of(Iterable<T> src) {
        /*
        return new PipeIterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return src.iterator();
            }
        };
         */
        return () -> src.iterator();
    }
    
    static <T> PipeIterable<T> generate(Supplier<T> supplier) {
        return () -> new GeneratorIterator<>(supplier);
    }
    
    static <T> PipeIterable<T> of(T... elems) {
        // esta é uma versão não lazy
        // construa uma versão lazy equivalente
        return of(Arrays.asList(elems));
    }
    
    static PipeIterable<Integer> range(int min, int max) {
        /*
        var l = new ArrayList();
        
        for(int i=min; i <= max; ++i) l.add(i);
        
        return of(l);
         */
        
        /**
          A versão comentada não funciona corretamente.
          Descomente, execute os testes e explique os
          efeitos observados
         */
//        var it = new Iterator<Integer>() {
//            int curr = min;
//            @Override
//            public boolean hasNext() {
//                return curr <= max;
//            }
//
//            @Override
//            public Integer next() {
//                if (!hasNext()) throw new IllegalStateException();
//                return curr++;
//            }
//        };
//        return () -> it;
        // versão correta
        return () -> new RangeIterator(min, max);
    }
    
    // intermediate operations
    
    default <U> PipeIterable<U> map(Function<T, U> mapper) {
        return () -> new MapIterator<>(this, mapper);
    }
    
    default PipeIterable<T> filter(Predicate<T> pred) {
        return () -> new FilterIterator<>(this, pred);
    }
    
}
