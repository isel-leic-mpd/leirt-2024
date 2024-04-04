package isel.leirt.mpd.weather.functional;

// a generic predicate, similar to java predicate.
// Better!
@FunctionalInterface
public interface MyPredicate<T> {
    boolean test(T t);
}
