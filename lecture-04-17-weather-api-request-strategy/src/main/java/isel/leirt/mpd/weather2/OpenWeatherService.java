package isel.leirt.mpd.weather2;

import isel.leirt.mpd.weather2.dto.LocationDto;
import isel.leirt.mpd.weather2.dto.WeatherInfoForecastDto;
import isel.leirt.mpd.weather2.model.DayInfo;
import isel.leirt.mpd.weather2.model.Location;
import isel.leirt.mpd.weather2.model.WeatherInfo;
import isel.leirt.mpd.weather2.queries.PipeIterable;

import java.time.LocalDate;
import java.util.Comparator;

import static isel.leirt.mpd.weather2.queries.PipeIterable.of;

public class OpenWeatherService {
	private  OpenWeatherWebApi api;

	public OpenWeatherService(OpenWeatherWebApi api) {
		this.api = api;
	}


	/**
	 *
	 * @param placeName
	 * @return
	 */
	public PipeIterable<Location> search(String placeName) {
			// CHANGE TO TURN LAZY
			return of(api.search(placeName))
			.map(this::dtoToLocation);
	}

	
	private PipeIterable<DayInfo> forecastAt(Location loc) {
		// CHANGE TO TURN LAZY
		return 	of(api.forecastWeatherAt(loc.getLatitude(), loc.getLongitude()))
				.map(dto -> dtoToDayInfo(dto, loc))
					  .distinct(Comparator.comparing(DayInfo::getDate));
	}

	private PipeIterable<WeatherInfo> weatherDetail(Double lat,
											   Double lon,
											   LocalDate day) {
		 return of(api.forecastWeatherAt(lon, lat))
				.filter(dto -> dto.dateTime().toLocalDate().equals(day))
				.map(this::dtoToWeatherInfo);
			 
	}

	private PipeIterable<WeatherInfo> weatherDetail(Location loc, DayInfo di) {
		return weatherDetail(loc.getLatitude(), loc.getLongitude(), di.getDate());
	}
	
	private  Location dtoToLocation(LocationDto dto) {
		return  new Location(dto.getName(),
			dto.getCountry(),
			dto.getLat(),
			dto.getLon()
		);
	}

	private  WeatherInfo dtoToWeatherInfo(WeatherInfoForecastDto dto) {
		return new WeatherInfo(
			dto.dateTime(),
			dto.temp(),
			dto.description(),
			dto.humidity(),
			dto.feelsLike());
	}

	// utilizem neste método a mesma técnica usada no Location
	// para que a obtenção das abservações de temperatura do dia só ocorra
	// quando for chamado o método temperatures de DayInfo
	public DayInfo dtoToDayInfo(WeatherInfoForecastDto dto, Location loc) {
		return new DayInfo(
			dto.dateTime().toLocalDate(),
			dto.maxTemp(),
			dto.minTemp(),
			dto.description()
		);
	}
}
