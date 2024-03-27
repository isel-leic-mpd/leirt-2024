package isel.leirt.mpd.weather;

import isel.leirt.mpd.weather.dto.*;
import isel.leirt.mpd.weather.functional.MyPredicate;
import isel.leirt.mpd.weather.queries.Queries;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static isel.leirt.mpd.weather.queries.Queries.*;
import static org.junit.jupiter.api.Assertions.*;

public class WeatherTests {
    private final static double LISBON_LAT  =  38.7071;
    private final static double LISBON_LONG = -9.1359;

    @Test
    // ok with current free key
    public void get_weather_at_lisbon_now() {
        OpenWeatherWebApi webApi = new OpenWeatherWebApi();
        WeatherInfoDto winfo = webApi.weatherAt(LISBON_LAT, LISBON_LONG );
        System.out.println(winfo);
    }
    
    @Test
    // ok with current free key
    public void get_air_pollution_in_lisbon_now() {
        OpenWeatherWebApi webApi = new OpenWeatherWebApi();
        
        PollutionInfoDto pi = webApi.airPollutionAt(
            LISBON_LAT, LISBON_LONG);
        
        
        System.out.println(pi);
    }
    
    @Test
    // ok with free current key
    public void get_air_pollution_history_by_period() {
        OpenWeatherWebApi webApi = new OpenWeatherWebApi();
        LocalDate start = LocalDate.of(2022, 03, 03);
        LocalDate end = LocalDate.of(2022, 04, 03);
        List<PollutionInfoDto> pinfo =
            webApi.pollutionHistoryAt(LISBON_LAT, LISBON_LONG,start,end);
        System.out.println(pinfo.size());
        for(PollutionInfoDto pi : pinfo) {
            System.out.println(pi);
        }
        assertEquals(745, pinfo.size() );
    }
    
    @Test
    // ok with free current key
    public void get_location_info_by_name() {
        OpenWeatherWebApi webApi = new OpenWeatherWebApi();
        String localName = "Lisboa";
        
        List<LocationDto> locations = webApi.search(localName);
        for(var loc : locations)
            System.out.println(loc);
        assertEquals(5, locations.size());
    }
    
    @Test
    // ok with free current key
    public void getForecastWeatherForLisbonTest() {
        int NDAYS = 5;
        int SAMPLES_PER_DAY = 8;
        
        OpenWeatherWebApi webApi = new OpenWeatherWebApi();
        List<LocationDto> locs = webApi.search("Lisbon");
        assertTrue(locs.size() > 0);
       
        LocationDto loc = null;
        for (var l : locs) {
            if (l.getCountry().equals("PT")) {
                loc = l;
            }
        }
        assertNotNull(loc);
        
        List<WeatherInfoForecastDto> winfo =
            webApi.forecastWeatherAt(loc.getLat(), loc.getLon());
        
        for(var wif : winfo) {
            System.out.println(wif);
        }
        
        assertEquals(NDAYS*SAMPLES_PER_DAY, winfo.size());
    }
    
    @Test
    public void get_sunny_periods_for_lisbon_forecast_test() {
        
        var weatherApi = new OpenWeatherWebApi();
        
        var forecast =
            weatherApi.forecastWeatherAt(LISBON_LAT, LISBON_LONG);
        
        var res =
            Queries.getSunnyPeriodsForForecast(forecast);
        
        System.out.println(res);
    }
    
    @Test
    public void get_rainy_periods_for_lisbon_forecast_test() {
        var weatherApi = new OpenWeatherWebApi();
        
        var forecast =
            weatherApi.forecastWeatherAt(LISBON_LAT, LISBON_LONG);
        
        var res =
            Queries.getRainyPeriodsForForecast(forecast);
        
        System.out.println(res);
    }
    @Test
    public void get_rainy_periods_for_lisbon_forecast_using_filter_test() {
        var weatherApi = new OpenWeatherWebApi();
        
        var forecast =
            weatherApi.forecastWeatherAt(LISBON_LAT, LISBON_LONG);
        
        MyPredicate<WeatherInfoForecastDto> pred =
            (p) -> p.description().contains("rain");
        
        var res = filter(forecast, pred);
        
        
        System.out.println(res);
    }
    
   
}
