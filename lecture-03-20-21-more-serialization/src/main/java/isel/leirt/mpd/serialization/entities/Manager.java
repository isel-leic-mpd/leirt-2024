package isel.leirt.mpd.serialization.entities;

public class Manager extends Person {
    private String department;
    public Manager(String name, Date birthday, String department) {
        super(name, birthday);
        this.department = department;
    }
    
    public Manager() {
    }
    
    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        return department.equals(((Manager) o).department);
    }
    
    @Override
    public String toString() {
        return String.format("%s, %s", super.toString(), department);
    }
}
