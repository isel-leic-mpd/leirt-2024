package isel.leirt.mpd.sequences;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface Sequence<T> {
    boolean tryAdvance(Consumer<T> action);
    
  
    static <T> Sequence<T> empty() {
        return cons -> false;
    }
    static <T> Sequence<T> of(Iterable<T> src) {
        // TODO
        return null;
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
}
