package isel.leirt.mpd.functional;

import isel.leirt.mpd.functional.data.Address;
import isel.leirt.mpd.functional.data.Person;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

//import java.util.Comparator;
//import static java.util.Comparator.comparing;

import isel.leirt.mpd.functional.Comparator;
import static isel.leirt.mpd.functional.Comparator.comparing;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComparatorTests {
    
    static List<Person> db = List.of(
        new Person("Carlos",
            LocalDate.of(1980, 3, 2),
            new Address("Coimbra", "3050")),
        new Person("Maria",
            LocalDate.of(2005, 10, 25),
            new Address("Coimbra", "2050"))
    );

    private Person greater(Person p1, Person p2, Comparator<Person> comparator) {
        return comparator.compare(p1,p2) > 0 ? p1 : p2;
    }

    @Test
    public void comparePersonsByNameTest() {
        Person carlos = db.get(0);
        Person maria = db.get(1);

        Comparator<Person> byName =
            (p1,p2) -> p1.getName().compareTo(p2.getName());
        
        Comparator<Person> byName2 =
            comparing(p -> p.getName());
        
        Comparator<Person> byName3 =
            comparing(Person::getName);

        Person res = greater(carlos,maria, byName2);

        assertEquals(maria, res);
    }

    @Test
    public void comparePersonsByAddressTest() {
        Person carlos = db.get(0);
        Person maria = db.get(1);

        Comparator<Person> byAddr =
            comparing(Person::getAddress,
                comparing(Address::getCity).thenComparing(Address::getZipCode));

        Person res = greater(carlos,maria, byAddr);

        assertEquals(carlos, res);
    }
}


