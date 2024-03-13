package isel.leirt.mpd;

import org.junit.jupiter.api.Test;
import static isel.leirt.mpd.reflection.ReflexUtils.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import isel.leirt.mpd.entities.*;

import java.lang.reflect.Field;

public class ReflectionTests {
    
    @Test
    public void stringImplementsComparableTest() {
        assertTrue(isImplementedBy(Comparable.class, String.class));
    }
    
    @Test
    public void instanceofBImplementsInterfaceXTest() {
        var b = new B();
        assertTrue(isImplementedBy(X.class, b.getClass()));
        
    }
    
    @Test
    public void checkIfAisASuperClassOfB() {
        assertTrue(isSubClass(A.class, C.class));
    }
    
    @Test
    public void changeWithReflectionFieldVOfClassA()
            throws SecurityException,
                   NoSuchFieldException,
                    IllegalAccessException {
        Object o  = new A();
        
        Class<?> oClass = o.getClass();
        Field f = oClass.getDeclaredField("v");
        f.setAccessible(true);
        f.set(o, Integer.valueOf(10));
        
        System.out.println("v value = " + ((A) o).getV());
    }
}
