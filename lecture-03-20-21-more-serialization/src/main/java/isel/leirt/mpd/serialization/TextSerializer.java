package isel.leirt.mpd.serialization;

import isel.leirt.mpd.serialization.annotations.Transient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.util.List;

import static isel.leirt.mpd.serialization.ReflexUtils.*;

public class TextSerializer {
    private static String TypeInfo = "__type";
    private static String NullObject = "__null";
    
    private static void indent(int level, PrintWriter writer) {
        for (int i=0; i < level; ++i) writer.write(" ");
    }
    
    private static void indentLine(int level, PrintWriter writer, Object content)  {
        indent(level, writer);
        writer.println(content);
    }
    
    private static void indentLine(int level, PrintWriter writer, String format, Object... args)  {
        indent(level, writer);
        writer.printf(format, args);
        writer.println();
    }
    
    private static String getClassName(BufferedReader reader) {
        try {
            String parts[] = reader.readLine().split(":");
            String typeTag = parts[0].trim();
            if (typeTag.equals(TypeInfo)) {
                return parts[1].trim();
            }
            throw new SerializerException("bad type info");
        }
        catch(IOException e) {
            throw new SerializerException("error reading class name", e);
        }
    }
    
    private static int getInt(BufferedReader reader) {
        try {
            String line = reader.readLine().trim();
            return Integer.parseInt(line);
        }
        catch(IOException e) {
            throw new SerializerException("error reading class name", e);
        }
    }
    
    private static void loadField(Field f,  Object obj, BufferedReader reader) {
        try {
            f.setAccessible(true);
            Class<?> fClass = f.getType();
            var line = reader.readLine();
            var parts = line.split(":");
            var fieldName = parts[0].trim();
            String fieldValue = null;
            if (parts.length == 2)
                fieldValue  = parts[1].trim();
            Object objFieldValue = null;
            if (!fieldName.equals(f.getName())) {
                throw new SerializerException("inconsistent name of field " + f.getName());
            }
            if (fClass.isPrimitive() || fClass == String.class) {
                // implemented just for int and String
                if (fClass == String.class) objFieldValue = fieldValue;
                else if (fClass == int.class)  objFieldValue = Integer.parseInt(fieldValue);
            }
            else if (fClass.isArray()){
                objFieldValue = loadArray(f, reader);
            }
            else {
                objFieldValue = loadObject(fClass, reader);
            }
            f.set(obj, objFieldValue);
        }
        catch(IOException e) {
            throw new UncheckedIOException("can't read field " + f.getName(), e);
        }
        catch(IllegalAccessException e) {
            throw new SerializerException("can't write on field" + f.getName(), e);
        }
      
    }
    
    /**
     * support for array deserializing
     * @param reader
     * @return
     */
    public static Object loadArray(Field f, BufferedReader reader) {
        var clsName = getClassName(reader);
        var objClass = classByName(clsName);
        if (objClass != f.getType()) {
            throw new SerializerException("Inconsistent array type!");
        }
     
        // read array size
        int len = getInt(reader);
        var componentType = objClass.componentType();
        Object objArray = createArray(componentType, len);
        
        for(int i=0; i < len; ++i) {
            Object elem = loadObject(componentType, reader);
            Array.set(objArray, i, elem );
        }
        return objArray;
        
    }
    
    public static <T>  T loadObject(Class<T> cls, BufferedReader reader) {
        var clsName = getClassName(reader);
        var objClass = classByName(clsName);
        T obj = (T) createInstance(objClass);
        
        List<Field> fields = getAllFields(obj.getClass());
        for (var f : fields) {
            if (isStatic(f)) continue;
            loadField(f, obj, reader);
        }
        return obj;
    }
    
    /**
     * support for array serializing
     * @param level
     * @param f
     * @param array
     * @param writer
     */
    public  static void  saveArray(int level, Field f, Object array, PrintWriter  writer ) {
        Class<?> fClass = f.getType();
        var len = Array.getLength(array);
        indentLine(level, writer, "%s: %s", TypeInfo, fClass.getName());
        indentLine(level, writer, len);
        for(int i= 0; i < len; ++i) {
            var elem = Array.get(array, i);
            saveObject(level, elem, writer );
        }
    }
    
    
    private static void saveField(int level, Field f, Object obj, PrintWriter writer) {
        try {
            f.setAccessible(true);
            var fValue = f.get(obj);
            var fClass = f.getType();
            if (fClass.isPrimitive() || fClass == String.class || fClass.isEnum()) {
                indentLine(level, writer, "%s: %s", f.getName(), fValue);
            }
            else if (fClass.isArray())  {
                indentLine(level, writer, f.getName() + ":");
                saveArray(level + 2, f, fValue, writer );
            }
            else {
                indentLine(level, writer, f.getName() + ":");
                saveObject(level + 2, fValue, writer);
            }
        }
        catch(IllegalAccessException e) {
            throw new SerializerException("can't access field" + f.getName(), e);
        }
    }
    
    private static void saveObject(int level, Object obj, PrintWriter writer) {
        Class<?> cls = obj.getClass();
        indentLine(level, writer, "%s: %s", TypeInfo, cls.getName());
        List<Field> fields = getAllFields(cls);
        for(var f : fields) {
            if (isStatic(f) || f.isAnnotationPresent(Transient.class)) continue;
            saveField(level, f, obj, writer);
        }
    }
    
    public static void saveObject(Object obj, PrintWriter writer) {
        saveObject(0, obj, writer);
    }
}
