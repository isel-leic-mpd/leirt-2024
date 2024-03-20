import isel.leirt.mpd.serialization.entities.Date;
import isel.leirt.mpd.serialization.entities.Employee;
import isel.leirt.mpd.serialization.entities.Manager;
import isel.leirt.mpd.serialization.entities.Person;
import org.junit.jupiter.api.Test;

import java.io.*;

import static isel.leirt.mpd.serialization.TextSerializer.*;

public class TextSerializerTests {
    
    @Test
    public void serializeManagerTest() {
        var p = new Manager("Alberto", new Date(16,3,1970), "Informatics");
        
        var out = new StringWriter();
        try (var writer = new PrintWriter(out)) {
            saveObject(p, writer);
        }
        
        System.out.println(out);
    }
    
    @Test
    public void serializeEmployeeTest() {
        var m = new Manager("Alberto", new Date(16,3,1970), "Informatics");
        var e = new Employee("Carlos", new Date(23,11,1980), m);
        
        var out = new StringWriter();
        try (var writer = new PrintWriter(out)) {
            saveObject(e, writer);
        }
        
        System.out.println(out);
    }
    
    @Test
    public void deserializeEmployeeTest() throws IOException  {
        var serText = """
            manager:
              department: Informatics
              name: Alberto
              birthday:
                day: 16
                month: 3
                year: 1970
              age: 54
            name: Carlos
            birthday:
              day: 23
              month: 11
              year: 1980
            age: 44
            """;
        
        try (BufferedReader reader = new BufferedReader(new StringReader(serText))) {
            Person p = loadObject(Employee.class, reader);
            System.out.println(p);
        }
    }
    
   
  
}
