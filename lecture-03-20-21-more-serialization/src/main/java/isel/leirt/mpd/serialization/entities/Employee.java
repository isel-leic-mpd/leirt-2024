package isel.leirt.mpd.serialization.entities;

public class Employee extends Person {
    
    // substitua o tipo do campo de Manager por Person e
    // explique os efeitos no teste "deserializeEmployeeTest"
    private Manager manager;
    
    public Employee() {
    
    }
    public Employee(String name, Date birthday, Manager manager) {
        super(name, birthday);
        this.manager = manager;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        return manager.equals(((Employee)o).manager);
    }
    
    @Override
    public String toString() {
        return String.format("%s, manager: %s", super.toString(), manager.getName());
    }
}
