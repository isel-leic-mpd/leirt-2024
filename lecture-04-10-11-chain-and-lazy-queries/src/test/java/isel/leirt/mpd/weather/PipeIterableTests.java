package isel.leirt.mpd.weather;

import isel.leirt.mpd.queries.PipeIterable;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static isel.leirt.mpd.queries.PipeIterable.*;
import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class PipeIterableTests {
    @Test
    public void fromVariableElementsToPipeIterableTest() {
        var p1 = PipeIterable.of(3,4,5);
        
        for (var e : p1) {
            System.out.println(e);
        }
        
        var p2   = PipeIterable.of(new int[] { 3, 4, 5});
        
        for (var e : p1) {
            System.out.println(e);
        }
        
        var p3   = PipeIterable.of();
        
        for (var e : p1) {
            System.out.println(e);
        }
    }
    
    @Test
    public void rangeTest() {
        var p1 = range(1,20);
        
        for (var e : p1) {
            System.out.println(e);
        }
        
        System.out.println("start");
        for (var e : p1) {
            System.out.println(e);
        }
        System.out.println("end");
        
        var it = p1.iterator();
        
        for(int i= 1; i <= 20; ++i) {
            System.out.println(it.next());
        }
        // bad use of iterator. We shoul allways call hasNext first!
        // uncommenting next line throw IllegalStateException!
        //System.out.println(it.next());
    }
    
    private static class WrapperInt {
        public int val;
        
        public WrapperInt(int v) {
            this.val = v;
        }
    }
    @Test
    public void testGenerate() {
        final WrapperInt w = new WrapperInt(2) ;
        Supplier<Integer> si = () -> {
            int curr = w.val;
            w.val += 2;
            return curr;
        };
        w.val += 1;
        PipeIterable<Integer> nrs =
            generate(si)
                .limit(10);
        
        var expected = List.of(2,4,6,8,10,12,14,16,18,20);
        assertEquals(expected, nrs.toList());
    }
    @Test
    public void testGenerate1() {
        int[] next = {2};
      
        Supplier<Integer> si = () -> {
            int curr = next[0];
            next[0] += 2;
            return curr;
        };
      
        PipeIterable<Integer> nrs =
            generate(si);
            
        
        var expected = List.of(2,4,6,8,10,12,14,16,18,20);
        assertEquals(expected, nrs.toList());
    }
    
    
    @Test
    public void testSkip(){
        List<Integer> nrs = List.of(1, 2, 3, 4, 5, 6, 7, 8);
        List<Integer> actual = of(nrs).skip(3).toList();
        var expected = List.of(4, 5, 6, 7, 8);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testLimit(){
      
        var nrs = List.of(1,3,5,7,9,11,13,15,17,19);
        var truncated = of(nrs).limit(3);
        assertEquals(3,truncated.count());
        assertEquals(List.of(1,3,5),truncated.toList());
    }
    
    @Test
    public void testTakeWhile(){
        var nrs = List.of(1,3,5,7,9,11,6,15,17,19);
        var expected = List.of(1,3,5,7,9);
        var lessThan10 = of(nrs).takeWhile(n -> n < 10);
        assertEquals(5,lessThan10.count());
        assertEquals(expected,lessThan10.toList());
    }
    
    @Test
    public void testFilter() {
        
        var  names = of( "Joao", "Carlos", "Jorge", "Luis");
        
        var jNames = names.filter(name -> name.startsWith("J"));
        
        assertEquals(2, jNames.count());
        assertEquals(List.of("Joao", "Jorge"), jNames.toList());
    }
    
    @Test
    public void testMap(){
        var words = List.of("super", "isel", "ola", "fcp");
        var actual =
            of(words).map(w -> w.length()).toList();
        var expected = List.of(5, 4, 3, 3);
        assertEquals(expected, actual);
    }
    
    private List<Integer> chars(String s) {
        return s.chars().boxed().collect(Collectors.toList());
    }
    
    @Test
    public void testFlatMap(){
        var words = List.of("super", "isel", "ola", "slb");
        var actual =
                of(words)
                .flatMap(w -> chars(w))
                .filter(c -> c == 's')
                .count();
        
        assertEquals(3, actual);
    }
    
    @Test
    public void testFlatMap2(){
        var  names = of( "Joao", "Carlos", "Jorge", "Luis");
        
        var jNames =
                  names
                  .flatMap(name -> name.startsWith("J") ? List.of(name) : List.of());
        
        
        assertEquals(2, jNames.count());
        assertEquals(List.of("Joao", "Jorge"), jNames.toList());
    }
    
    @Test
    public void testMax(){
        Random r = new Random();
        List<Integer> nrs = generate(() -> r.nextInt(100))
                                    .limit(100).toList();
        var m = of(nrs).max(Integer::compare);
        nrs.sort(Integer::compare);
        
        m.ifPresent(out::println);
        assertTrue(m.isPresent());
        assertEquals(nrs.get(nrs.size()-1), m.get());
    }
    
}
