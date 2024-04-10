package isel.leirt.mpd.weather;

import isel.leirt.mpd.queries.PipeIterable;
import org.junit.jupiter.api.Test;

import static isel.leirt.mpd.queries.PipeIterable.range;


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
        System.out.println(it.next());
    }
    
   
}
