package isel.leirt.mpd.sequences;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public interface Sequence<T> {
    boolean tryAdvance(Consumer<T> action);
    
    static <T> Sequence<T> empty() {
        return cons -> false;
    }
    
    static <T> Sequence<T> of(Iterable<T> src) {
        Iterator<T> it = src.iterator();
        return cons -> {
            if (!it.hasNext()) return false;
            cons.accept(it.next());
            return true;
        };
    }
    
    static <T> Sequence<T> iterate(T seed, UnaryOperator<T> oper) {
        // TODO
        return null;
    }
    
    
    default <U> Sequence<U> map(Function<T,U> mapper) {
        return cons -> tryAdvance( t -> cons.accept(mapper.apply(t)));
    }
    
    
    default void forEach(Consumer<T> action) {
        while(tryAdvance(action));
    }
    
    default Sequence<T> takeWhile (Predicate<T> pred) {
        boolean done[] = {false};
        return cons -> {
            if (done[0]) return false;
           
            if (!tryAdvance(t -> {
                if (pred.test(t)) {
                    cons.accept(t);
                    
                }
                else {
                    done[0] = true;
                }
            })) done[0] = true;
            return !done[0];
        };
    }
}
