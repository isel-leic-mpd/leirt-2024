package isel.leirt.mpd.serialization.entities;

import isel.leirt.mpd.serialization.annotations.Transient;

import java.time.LocalDate;



public abstract class Person {
    // category names
    private static final String Boss = "Boss";
    private static final String Manager = "Manager";
    private static final String Employer = "Employer";
    
    private String name;
    private Date birthday;
    
    @Transient
    private int age;
    
    private static int calcAge(Date birthday) {
        return LocalDate.now().getYear() - birthday.getYear();
    }
    
    protected Person() {
    
    }
    
    protected Person(String name, Date birthday) {
        this.name = name;
        this.birthday = birthday;
        this.age = calcAge(birthday);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != getClass()) return false;
        Person other = (Person) o;
        return name.equals(other.name) && birthday.equals(other.birthday);
    }
    
    @Override
    public String toString() {
        return String.format("%s, %s, %d anos", name, birthday.toString(), getAge());
    }
    
    public String getName() { return name; }
    
    public int getAge() {
        if (age == 0) {
            age = calcAge(birthday);
        }
        return age;
    }
    
    public Date getBirthday() {
        return birthday;
    }
}
