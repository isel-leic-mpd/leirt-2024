package isel.leirt.mpd.serialization.entities;

public class Date {
    private int day, month, year;
    
    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }
    
    public Date() {
    
    }
    @Override
    public boolean equals(Object o) {
        if (o.getClass() != getClass()) return false;
        Date other = (Date) o;
        return day == other.day && month == other.month && year == other.year;
    }
    
    @Override
    public String toString() {
        return String.format("%02d/%02d/%04d", day, month, year);
    }
    
    public int getYear() { return year; }
    public int getMonth() { return month; }
    public int getDay() { return day; }
    
}
