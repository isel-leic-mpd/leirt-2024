package isel.leirt.mpd.reflection;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.List;

public class ReflexUtils {
    public static boolean isImplementedBy( Class<?> interfaceType, Class<?> classType) {
        var currClass = classType;
        while(currClass != null) {
            Class<?>[] implInterfaces = currClass.getInterfaces();
            for (var interfac : implInterfaces) {
                if (interfac == interfaceType) return true;
            }
            currClass = currClass.getSuperclass();
        }
      
        return false;
    }
    
    public static boolean isImplementedBy0( Class<?> interfaceType, Class<?> classType) {
        
        Class<?>[] implInterfaces = classType.getInterfaces();
        for (var interfac : implInterfaces) {
            if (interfac == interfaceType) return true;
        }
        
        
        return false;
    }
    
    public static boolean isSubClass(Class<?> classType1, Class<?> classType2) {
        Class<?> currSuper = classType2.getSuperclass();
        while(currSuper != null) {
            if ( currSuper == classType1) return true;
            currSuper = currSuper.getSuperclass();
        }
       
        return false;
    }
    
    public static boolean isInstanceOf(Object o, Class<?> type) {
        if (o == null) throw new IllegalArgumentException("object should not be null");
        Class<?> objClass = o.getClass();
        return objClass == type ||
               (type.isInterface() && isImplementedBy(type, objClass)) ||
               (!type.isInterface() && isSubClass(type, objClass));
    }
    
    List<Field> getAllFields(Object o) {
        // TO IMPLEMENT
        return null;
    }
}
