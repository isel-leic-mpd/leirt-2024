package isel.leirt.mpd.weather4.queries;

import isel.leirt.mpd.weather4.queries.iterators.*;

import java.util.*;
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
    
    @SuppressWarnings("unchecked")
    static <T> PipeIterable<T> of(T... elems) {
        // esta é uma versão não lazy
        // construa uma versão lazy equivalente
        // return of(Arrays.asList(elems));
        // return () -> of(Arrays.asList(elems)).iterator()
        return () -> new Iterator<T>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < elems.length;
            }

            @Override
            public T next() {
               if (!hasNext()) throw new IllegalStateException();
               return elems[index++];
            }
        };
    }
    
    @SuppressWarnings("unchecked")
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
    
    @SuppressWarnings("unchecked")
    default <U> PipeIterable<U> map(Function<T, U> mapper) {
        return () -> new MapIterator<>(this, mapper);
    }
    
    @SuppressWarnings("unchecked")
    default PipeIterable<T> filter(Predicate<T> pred) {
        return () -> new FilterIterator<>(this, pred);
    }

    @SuppressWarnings("unchecked")
    default <U> PipeIterable<U> flatMap(Function<T, Iterable<U>> mapper) {
        return () -> new FlatmapIterator(this, mapper);
    }

    default  PipeIterable<T> distinct(Comparator<T> cmp){
        Set<T> cache = new TreeSet<T>(cmp);

        return () ->  new Iterator<T>() {
            Optional<T> curr = Optional.empty();
            Iterator<T> itSrc = PipeIterable.this.iterator();

            @Override
            public boolean hasNext() {
                if (curr.isPresent()) return true;
                while(itSrc.hasNext()) {
                    var n = itSrc.next();

                    if (!cache.contains(n)) {
                        cache.add(n);
                        curr = Optional.of(n);
                        return true;
                    }
                }
                return false;
            }

            @Override
            public T next() {
                if (!hasNext()) throw new IllegalStateException();
                var n = curr.get();
                curr = Optional.empty();
                return n;
            }
        };

    }

    @SuppressWarnings("unchecked")
    default PipeIterable<T> takeWhile(Predicate<T> pred){
        return () -> new TakeWhileIterator(this, pred);
    }

    default  PipeIterable<T> skip( int nr) {
        // To Implement
        return null;
    }

    default PipeIterable<T> limit(int lim) {
        // To Implement
        return null;
    }

    // terminal operations

    default long count() {
        var c = 0L;
        for(var v : this) {
            c++;
        }
        return c;
    }

    default List<T> toList() {
        var l = new ArrayList<T>();
        var it = iterator();

        while(it.hasNext()) {
            l.add(it.next());
        }
//        while(it.hasNext()) {
//            l.add(it.next());
//        }
//
        return l;
    }

    default Optional<T> first() {
        Iterator<T> it = iterator();
        if (!it.hasNext()) {
            Optional<T> o =  Optional.empty();
            //var v = o.get();
            return o;
        }
        else return Optional.ofNullable(it.next());
    }

    default Optional<T> max(Comparator<T> cmp) {
        Iterator<T> it = iterator();
        if (!it.hasNext()) return Optional.empty();
        var m = it.next();
        while(it.hasNext()) {
            var t = it.next();
            if (cmp.compare(t, m) > 0) m = t;
        }
        return Optional.of(m);
    }

}
