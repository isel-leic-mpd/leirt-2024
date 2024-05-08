package isel.leirt.mpd.streams;

import isel.leirt.mpd.utils.Primes;
import org.junit.jupiter.api.Test;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StreamTests {
   
    
    @Test
    public void showFirst5Primes() {
        var primes = Stream.iterate(2l, Primes::nextPrime);
        
        primes
            .limit(5)
            .forEach(System.out::println);
        
        // the assertion fails because the stream admits only one terminal operation
        // In this case, the foreach above
        assertEquals(5, primes.count());
    }
    
    @Test
    public void getPrimesTill_10_000_000() {
        
        var primes =
            LongStream.range(2, 10_000_000)
                .filter(Primes::isPrime);
        
                
        long startTime = System.currentTimeMillis();
        
        var primesArray = primes
                                 // the parallel operation speedup the
                                 // stream consume by using all cores
                                 // in the process
                                .parallel().
                                toArray();
        
        System.out.println("done in " + (System.currentTimeMillis()-startTime) + "ms!");
        /*
            for(var p : primesArray) {
                System.out.println(p);
            }
        */
    }
    
    class Pair<T,U> {
        public final T first;
        public final U second;
        
        public Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }
        
        @Override
        public String toString()  {
            return String.format("{%s, %s}", first, second);
        }
    }
    
    @Test
    public void produceCombinationsPairsOfIntsBteween1And10() {
        var combs =
            IntStream.rangeClosed(1,10).mapToObj(n -> n)
                .flatMap(n1 -> IntStream.rangeClosed(n1 + 1, 10).boxed()
                           .map(n2 -> new Pair(n1, n2)));
        
        var combsList = combs.toList();
        
        for(var p : combsList) {
            System.out.println(p);
        }
        
        System.out.println("combs number = " + combsList.size());
    }
  
    
    @Test
    public void getSpliteratorStreamTest() {
        var names =
            Stream.of("Leandro", "Paulo", "Leonel")
            .peek(System.out::println)
            .filter(name -> name.startsWith("L"));
        
        System.out.println("Start!");
        var streamIt = names.spliterator();
        
        // iterate it!
        // same as: streamIt.forEachRemaining(System.out::println));
        while(streamIt.tryAdvance(System.out::println));
        
    }
    
    @Test
    public void getIteratorFromStreamTest() {
        var names = Stream.of("Leandro", "Paulo", "Leonel")
                          .peek(System.out::println)
                          .filter(name -> name.startsWith("L"));
        
        // TO COMPLETE
        
    }
    
    
   
 
}
