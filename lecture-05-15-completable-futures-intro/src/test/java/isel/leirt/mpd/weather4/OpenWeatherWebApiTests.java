package isel.leirt.mpd.weather4;

import isel.leirt.mpd.weather4.dto.LocationDto;
import isel.leirt.mpd.weather4.dto.PollutionInfoDto;
import isel.leirt.mpd.weather4.dto.WeatherInfoDto;
import isel.leirt.mpd.weather4.dto.WeatherInfoForecastDto;
import isel.leirt.mpd.weather4.requests.HttpRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OpenWeatherWebApiTests {
    private final static double LISBON_LAT  =  38.7071;
    private final static double LISBON_LONG = -9.1359;
   
    
    @Test
    // ok with current free key
    public void get_weather_at_lisbon_now() {
        OpenWeatherWebApi webApi = new OpenWeatherWebApi(new HttpRequest());
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
    public void getForecastWeatherForPortimaoTest() {
        int NDAYS = 5;
        int SAMPLES_PER_DAY = 8;
        
        OpenWeatherWebApi webApi = new OpenWeatherWebApi();
        List<LocationDto> locs = webApi.search("Portimao");
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
        
        var res = forecast.stream()
                  .filter(f -> f.description().contains("sunny"));
           
        System.out.println(res);
    }
    
    @Test
    public void get_rainy_periods_for_lisbon_forecast_test() {
        var weatherApi = new OpenWeatherWebApi();
        
        var forecast =
            weatherApi.forecastWeatherAt(LISBON_LAT, LISBON_LONG);
        
        var res = forecast.stream()
               .filter(f -> f.description().contains("rain"));
        
        System.out.println(res);
    }
    
    @Test
    public void get_rainy_periods_for_lisbon_forecast_using_filter_test() {
        var weatherApi = new OpenWeatherWebApi();
       
        var res1 =
            weatherApi.forecastWeatherAt(LISBON_LAT, LISBON_LONG).stream()
            .filter(p -> p.description().contains("rain"))
            .max(Comparator.comparingDouble(WeatherInfoForecastDto::maxTemp));
        System.out.println(res1);
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
    public void date_time_for_not_good_ozone_in_lisbon_at_march_2024() {
        var weatherApi = new OpenWeatherWebApi();
        
        var pollutionData =
            weatherApi.pollutionHistoryAt(LISBON_LAT, LISBON_LONG,
                LocalDate.of(2024, 03, 01),
                LocalDate.of(2024,03, 25 ));
        
        var r1 =
            pollutionData.stream()
            .filter(p -> !p.isGoodO3());
        
        r1.forEach( item -> {
            System.out.println(item);
            System.out.println();
        });
    }
    
    static class LocalTemp {
        public final LocationDto local;
        public final WeatherInfoForecastDto wi;
        
        public LocalTemp(LocationDto local, WeatherInfoForecastDto wi) {
            this.local = local;
            this.wi = wi;
        }
        
        @Override
        public String toString()  {
            return String.format("location: %s\nweather info:\n%s\n",
                local.toString(), wi.toString());
        }
    }
    
    
    @Test
    public void maxForecastTemperatureForLocalsNamedLisbon() {
        var weatherApi = new OpenWeatherWebApi();
       
        var mt =
            weatherApi.search("Lisbon").stream()
            .flatMap( l ->
                  weatherApi.forecastWeatherAt(l.getLat(), l.getLon()).stream()
                 .map(wi -> new LocalTemp(l, wi)))
            .max((lt1, lt2) -> (int) (lt1.wi.maxTemp() - lt2.wi.maxTemp()));

        System.out.println(mt.map(lt -> lt.toString()).orElse("none"));
    }
   
}
