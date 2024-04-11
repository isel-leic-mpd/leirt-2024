package isel.leirt.mpd.weather;

import isel.leirt.mpd.weather.dto.LocationDto;
import isel.leirt.mpd.weather.dto.PollutionInfoDto;
import isel.leirt.mpd.weather.dto.WeatherInfoDto;
import isel.leirt.mpd.weather.dto.WeatherInfoForecastDto;
import isel.leirt.mpd.weather.queries.Queries;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static isel.leirt.mpd.weather.queries.Queries.*;
import static org.junit.jupiter.api.Assertions.*;

public class WeatherTests {
    private final static double LISBON_LAT  =  38.7071;
    private final static double LISBON_LONG = -9.1359;
    
    @Test
    public void get_rainy_periods_for_lisbon_forecast_using_filter_test() {
        var weatherApi = new OpenWeatherWebApi();
      
        var res1 = map(
            filter(
                weatherApi.forecastWeatherAt(LISBON_LAT, LISBON_LONG),
                p -> p.description().contains("rain")
            ),
            wi -> wi.maxTemp()
        );
        
        
        var res2 =
            max(
                res1
            );
        System.out.println(res2);
    }
    
    
    @Test
    public void maxForecastTemperatureForLocalsNamedLisbon() {
        var weatherApi = new OpenWeatherWebApi();
       
        var mt = max(
                            map(
                                flatMap(
                                     weatherApi.search("Lisbon"),
                                     l -> weatherApi.forecastWeatherAt(l.getLat(), l.getLon())
                                ),
                                (WeatherInfoForecastDto wi) -> wi.maxTemp()
                            )
                        );
        System.out.println(mt);
    }
   
}
