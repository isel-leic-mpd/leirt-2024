import isel.leirt.mpd.serialization.entities.*;
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
            __type: isel.leirt.mpd.serialization.entities.Employee
           manager:
             __type: isel.leirt.mpd.serialization.entities.Manager
             department: Informatics
             name: Alberto
             birthday:
               __type: isel.leirt.mpd.serialization.entities.Date
               day: 16
               month: 3
               year: 1970
             age: 54
           name: Carlos
           birthday:
             __type: isel.leirt.mpd.serialization.entities.Date
             day: 23
             month: 11
             year: 1980
           age: 44
            """;
        
        try (BufferedReader reader = new BufferedReader(new StringReader(serText))) {
            Person p = (Person) loadObject(Employee.class, reader);
            System.out.println(p);
        }
    }
    
    @Test
    public void serializeCompanyTest() {
        var m = new Manager("Alberto", new Date(16,3,1970), "Informatics");
        var e = new Employee("Carlos", new Date(23,11,1980), m);
        
        var persons = new Person[] {
            m,
            e
        };
        
        var company = new Company("IselMPD", persons);
        var out = new StringWriter();
        try (var writer = new PrintWriter(out)) {
            saveObject(company, writer);
        }
        
        System.out.println(out);
    }
    
    @Test
    public void loadCompanyFromText() throws IOException {
        var text = """
            __type: isel.leirt.mpd.serialization.entities.Company
            people:
              __type: [Lisel.leirt.mpd.serialization.entities.Person;
              2
              __type: isel.leirt.mpd.serialization.entities.Manager
              department: Informatics
              name: Alberto
              birthday:
                __type: isel.leirt.mpd.serialization.entities.Date
                day: 16
                month: 3
                year: 1970
              age: 54
              __type: isel.leirt.mpd.serialization.entities.Employee
              manager:
                __type: isel.leirt.mpd.serialization.entities.Manager
                department: Informatics
                name: Alberto
                birthday:
                  __type: isel.leirt.mpd.serialization.entities.Date
                  day: 16
                  month: 3
                  year: 1970
                age: 54
              name: Carlos
              birthday:
                __type: isel.leirt.mpd.serialization.entities.Date
                day: 23
                month: 11
                year: 1980
              age: 44
            name: IselMPD
            
            """;
        
        try (BufferedReader reader = new BufferedReader(new StringReader(text))) {
            Company  c = (Company) loadObject(Company.class, reader);
            System.out.println(c);
        }
    }
   
  
}
