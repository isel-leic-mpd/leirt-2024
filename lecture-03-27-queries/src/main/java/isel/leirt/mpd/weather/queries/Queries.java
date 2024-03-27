package isel.leirt.mpd.weather.queries;

import isel.leirt.mpd.weather.dto.PollutionInfoDto;
import isel.leirt.mpd.weather.dto.WeatherInfoDto;
import isel.leirt.mpd.weather.dto.WeatherInfoForecastDto;
import isel.leirt.mpd.weather.functional.MyPredicate;
import isel.leirt.mpd.weather.functional.WeatherInfoForecastPredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Queries {
    private final static double LISBON_LAT  =  38.7071;
    private final static double LISBON_LONG = -9.1359;
    
    public static Iterable<WeatherInfoForecastDto> getSunnyPeriodsForForecast(
        Iterable<WeatherInfoForecastDto> forecast) {

        var result = new ArrayList<WeatherInfoForecastDto>();
        for (var f : forecast) {
            if (f.description().contains("sun"))
                result.add(f);
        }
        return result;
    }
    
    public static Iterable<WeatherInfoForecastDto> getRainyPeriodsForForecast(
        List<WeatherInfoForecastDto> forecast) {
        var result = new ArrayList<WeatherInfoForecastDto>();
        for (var f : forecast) {
            if (f.description().contains("rain"))
                result.add(f);
        }
        return result;
    }
    
    
    // a filter just for WeatherInfoForecastDto sequences
    // not bery usefull!
    public static Iterable<WeatherInfoForecastDto>
    filter(Iterable<WeatherInfoForecastDto> src,
           WeatherInfoForecastPredicate pred) {
        var result = new ArrayList<WeatherInfoForecastDto>();
        for (var f : src) {
            if (pred.test(f))
                result.add(f);
        }
        return result;
    }

    public static Iterable<PollutionInfoDto> getPoorO3ForPollutionData(
            Iterable<PollutionInfoDto> pollutionData) {
        // TODO
        return null;
    }
    
    public static Iterable<PollutionInfoDto> getGoodO3ForPollutionData(
        Iterable<PollutionInfoDto> pollutionData) {
        var result = new ArrayList<PollutionInfoDto>();
        for (var p : pollutionData) {
            if (p.isGoodO3())
                result.add(p);
        }
        return result;
    }
    
    // a generic filter using our predicate interface
    // we could just replace it with java predicate
    public static <T> Iterable<T> filter(Iterable<T> src,
           MyPredicate<T> pred) {
        var result = new ArrayList<T>();
        for (var f : src) {
            if (pred.test(f))
                result.add(f);
        }
        return result;
    }

    public Iterable<Double> getTemperaturesOutOfIntervalInForecast(
        Iterable<WeatherInfoForecastDto> seq, double min, double max) {
        List<Double> temps = new ArrayList<>();
        // TODO
        return temps;
    }
    
  
    
}
