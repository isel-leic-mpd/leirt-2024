package isel.leirt.mpd.serialization;

import isel.leirt.mpd.serialization.annotations.Transient;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessFlag;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflexUtils {
    
    public static List<Field> getAllFields(Class<?> cls) {
        var currCls = cls;
        var fields = new ArrayList<Field>();
        while (currCls != Object.class) {
            fields.addAll(Arrays.asList(currCls.getDeclaredFields()));
            currCls = currCls.getSuperclass();
        }
        return fields;
    }
    
    public static boolean hasCharacteristics(Field f, AccessFlag... characteristics) {
        for (var c : characteristics) {
            if (!f.accessFlags().contains(c)) return false;
        }
        return true;
    }
    
    public static boolean isPrivate(Field f) {
        
        return hasCharacteristics(f, AccessFlag.PRIVATE);
    }
    
    public static boolean isStatic(Field f) {
        
        return hasCharacteristics(f, AccessFlag.STATIC);
    }
   
    public static <T> T createInstance(Class<T> objClass) {
        try {
            var ctor = objClass.getConstructor();
            return ctor.newInstance();
        }
        catch(NoSuchMethodException e) {
            throw new RuntimeException( "No default constructor on class " + objClass.getName(), e);
        }
        catch(InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException( "error invoking constructor for class " + objClass.getName(), e);
        }
    }
    
    public static Object createArray(Class<?> objClass, int len) {
        return Array.newInstance(objClass, len);
    }
    
    public static Class<?> classByName(String className) {
        try {
            return Class.forName(className);
        }
        catch(ClassNotFoundException e) {
            throw new RuntimeException( "can't load class " + className, e);
        }
    }
    
}
