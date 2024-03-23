package isel.leirt.mpd.serialization.entities;

public class Company {
    private Person[] people;
    private String name;
    
    public Company() {
    
    }
    public Company(String name, Person[] people) {
        this.name = name;
        this.people = people;
    }
    
    public String getName() {
        return name;
    }
    
    public Person[] getPeople() {
        return people;
    }
}
