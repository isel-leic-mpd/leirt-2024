package isel.leirt.mpd.weather2.model;

import isel.leirt.mpd.weather2.queries.PipeIterable;

import java.util.function.Function;

public class Location {

	private String name;
	private String country;
	private double latitude;
	private double longitude;

	private PipeIterable<DayInfo> forecast;

	public Location(String name,
	                String country,
	                double latitude,
	                double longitude) {
		this.name = name;
		this.country = country;

		this.latitude = latitude;
		this.longitude = longitude;
	}

	// acessors
	public String getName()         { return name; }
	public String getCountry()      { return country; }
	public double getLatitude()     { return latitude; }
	public double getLongitude()    { return longitude; }

	public PipeIterable<DayInfo> forecast()  {
		// TO CHANGE
		return forecast;
	}

	@Override
	public String toString() {
		return "{"
			+ name
			+ ", country=" + getCountry()
			+ ", latitude=" + getLatitude()
			+ ", longitude=" + getLongitude()
			+ "}";
	}

}
