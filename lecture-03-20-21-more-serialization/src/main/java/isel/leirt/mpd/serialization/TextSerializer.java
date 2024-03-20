package isel.leirt.mpd.serialization;

import isel.leirt.mpd.serialization.annotations.Transient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.util.Collection;
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
    
    private static String getClassNameOrNull(BufferedReader reader) {
        try {
            String parts[] = reader.readLine().split(":");
            if(parts[0].trim().equals(NullObject)) {
                return NullObject;
            }
            if (parts[0].trim().equals(TypeInfo)) {
                return parts[1].trim();
            }
            throw new SerializerException("bad type info");
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
                throw new SerializerException("incoerent name of field " + f.getName());
            }
            if (fClass.isPrimitive() || fClass == String.class) {
                // implemented just for int and String
                if (fClass == String.class) objFieldValue = fieldValue;
                else if (fClass == int.class)  objFieldValue = Integer.parseInt(fieldValue);
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
    
    public static <T>  T loadObject(Class<T> cls, BufferedReader reader) {
        T obj = (T) createInstance(cls);
        List<Field> fields = getAllFields(obj.getClass());
        for (var f : fields) {
            if (isStatic(f)) continue;
            loadField(f, obj, reader);
        }
        return obj;
    }
    
    private static void saveField(int level, Field f, Object obj, PrintWriter writer) {
        try {
            f.setAccessible(true);
            var fValue = f.get(obj);
            var fClass = f.getType();
            if (fClass.isPrimitive() || fClass == String.class || fClass.isEnum()) {
                indentLine(level, writer, "%s: %s", f.getName(), fValue);
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
        List<Field> fields = getAllFields(cls);
        for(var f : fields) {
            if (isStatic(f)) continue;
            saveField(level, f, obj, writer);
        }
    }
    
    public static void saveObject(Object obj, PrintWriter writer) {
        saveObject(0, obj, writer);
    }
}
