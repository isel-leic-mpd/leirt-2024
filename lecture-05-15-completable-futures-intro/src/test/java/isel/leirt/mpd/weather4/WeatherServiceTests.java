package isel.leirt.mpd.weather4;

import isel.leirt.mpd.weather4.model.DayInfo;
import isel.leirt.mpd.weather4.model.Location;
import isel.leirt.mpd.weather4.model.WeatherInfo;
import isel.leirt.mpd.weather4.requests.HttpRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class WeatherServiceTests {

	@Test
	public void get_locations_named_lisbon() {
		
		OpenWeatherService service =
			new OpenWeatherService(
				new OpenWeatherWebApi()
			);
		
		Stream<Location> locations =
			service.search("Lisboa");
 		locations.forEach(System.out::println);
		
	}
	
	@Test
	public void get_locations_named_porto() {
		
		OpenWeatherService service =
			new OpenWeatherService(
				new OpenWeatherWebApi()
			);
		
		Stream<Location> locations =
			service.search("Porto");
		
		locations.forEach(System.out::println);
		
		
	}


	@Test
	public void getForecastForLisbonTest() {

		OpenWeatherService service =
			new OpenWeatherService(
				new OpenWeatherWebApi( ));

		// TODO
		Stream<DayInfo> forecastWeather =
			service.search("Lisbon")
		   	.filter(l -> l.getCountry().equals("PT"))
			.flatMap(l -> l.forecast());

		List<DayInfo>  forecastList = forecastWeather.toList();

		long nDays = forecastList.size();
		assertEquals(6, nDays);

		System.out.println("DayInfo list size: " + nDays);
		forecastList.forEach(System.out::println);
	}

	@Test
	public void getForecastDetailForTomorrowAtLisbonTest() {
		var service =
			new OpenWeatherService(new OpenWeatherWebApi(new HttpRequest()));

		// TODO
		List<WeatherInfo> tomorrowTemps =
			service.search("Lisboa")
				   .filter(l -> l.getCountry().equals("PT"))
				   .flatMap(l -> l.forecast())
				   .filter(di -> di.getDate().equals(LocalDate.now().plusDays(1)))
				   .flatMap(di -> di.temperatures())
				   .toList();
		
		assertEquals(8, tomorrowTemps.size());
		tomorrowTemps.forEach(System.out::println);
	}
}
