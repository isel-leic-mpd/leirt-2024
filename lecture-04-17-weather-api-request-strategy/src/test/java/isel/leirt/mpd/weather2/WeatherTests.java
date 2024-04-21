package isel.leirt.mpd.weather2;

import static isel.leirt.mpd.weather2.queries.PipeIterable.*;
import isel.leirt.mpd.weather2.dto.*;
import isel.leirt.mpd.weather2.requests.HttpRequest;
import isel.leirt.mpd.weather2.requests.MockRequest;
import isel.leirt.mpd.weather2.requests.Request;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WeatherTests {
    private final static double LISBON_LAT  =  38.7071;
    private final static double LISBON_LONG = -9.1359;
    
    private Reader retrieve(String path) {
        try {
            URL url = new URL(path);
            return new InputStreamReader(url.openStream());
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    @Test
    // ok with current free key
    public void get_weather_at_lisbon_now() {
        WeatherTests wt = new WeatherTests();
        
        Request req = wt::retrieve; //s -> retrieve(s);
        
        OpenWeatherWebApi webApi = new OpenWeatherWebApi(this::retrieve);
        WeatherInfoDto winfo = webApi.weatherAt(LISBON_LAT, LISBON_LONG );
        System.out.println(winfo);
    }
    
    @Test
    public void save_weather_at_lisbon_now() {
        OpenWeatherWebApi webApi = new OpenWeatherWebApi(new HttpRequest());
        webApi.saveWeatherAt(LISBON_LAT, LISBON_LONG );
    }
    
    @Test
    public void get_weather_at_lisbon_now_from_cache() {
        OpenWeatherWebApi webApi = new OpenWeatherWebApi(new MockRequest());
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
    public void getForecastWeatherForLisbonTest() {
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
        
        var res = of(forecast)
                  .filter(f -> f.description().contains("sunny"));
           
        System.out.println(res);
    }
    
    @Test
    public void get_rainy_periods_for_lisbon_forecast_test() {
        var weatherApi = new OpenWeatherWebApi();
        
        var forecast =
            weatherApi.forecastWeatherAt(LISBON_LAT, LISBON_LONG);
        
        var res = of(forecast)
               .filter(f -> f.description().contains("rain"));
        
        System.out.println(res);
    }
    
    @Test
    public void get_rainy_periods_for_lisbon_forecast_using_filter_test() {
        var weatherApi = new OpenWeatherWebApi();
       
        var res1 =
            of(weatherApi.forecastWeatherAt(LISBON_LAT, LISBON_LONG))
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
            of(pollutionData)
            .filter(p -> !p.isGoodO3());
        
        for(var item:r1) {
            System.out.println(item);
            System.out.println();
        }
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
                    of(weatherApi.search("Lisbon"))
                    .flatMap( l -> of(weatherApi.forecastWeatherAt(l.getLat(), l.getLon()))
                                   .map(wi -> new LocalTemp(l, wi)))
                    .max((lt1, lt2) -> (int) (lt1.wi.maxTemp() - lt2.wi.maxTemp()));
        
        System.out.println(mt.map(lt -> lt.toString()).orElse("none"));
    }
   
}
