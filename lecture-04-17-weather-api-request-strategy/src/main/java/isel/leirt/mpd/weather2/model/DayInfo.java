package isel.leirt.mpd.weather2.model;

import isel.leirt.mpd.weather2.queries.PipeIterable;

import java.time.LocalDate;
import java.util.function.Function;

public class DayInfo {
	private LocalDate date;
	private double maxTempC;
	private double minTempC ;
	private String description;

	private PipeIterable<WeatherInfo> temperatures;

	public DayInfo(LocalDate date, double maxTempC,
				   double minTempC, String description) {
		this.date = date;
		this.maxTempC = maxTempC;
		this.minTempC = minTempC;
		this.description = description;
	}

	// accessors
	public LocalDate getDate()      { return date; }
	public double getMaxTemp()      { return maxTempC; }
	public double getMinTemp()      { return minTempC; }
	public String getDescription()  { return description; }

	public PipeIterable<WeatherInfo> temperatures() {
		// TO CHANGE
		return temperatures;
	}
	
	@Override
	public String toString() {
		return "{"
			+ date
			+ ", "				+ description
			+ ", min="          + minTempC
			+ ", max="          + maxTempC
			+ "}";
	}
}
