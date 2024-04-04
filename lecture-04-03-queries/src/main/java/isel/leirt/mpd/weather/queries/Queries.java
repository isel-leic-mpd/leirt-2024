package isel.leirt.mpd.weather.queries;

import isel.leirt.mpd.weather.dto.PollutionInfoDto;
import isel.leirt.mpd.weather.dto.WeatherInfoDto;
import isel.leirt.mpd.weather.dto.WeatherInfoForecastDto;
import isel.leirt.mpd.weather.functional.MyPredicate;
import isel.leirt.mpd.weather.functional.WeatherInfoForecastPredicate;

import java.util.ArrayList;
import java.util.Iterator;
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
            if (f.description().contains("sun") ||
                f.description().contains("scattered clouds"))
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
    
    
    // a filter just for WeatherInfoForecastDto sequences.
    // Not very useful!
    public static Iterable<WeatherInfoForecastDto>
    filter0(Iterable<WeatherInfoForecastDto> src,
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
    
    public Iterable<Double> getTemperaturesOutOfIntervalInForecast(
        Iterable<WeatherInfoForecastDto> seq, double min, double max) {
        List<Double> temps = new ArrayList<>();
        // TODO
        return temps;
    }
    
    // intermediate operations
    
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
    
    
    public static <T,U> Iterable<U> map(Iterable<T> src,
                                        Function<T,U> mapper) {
        var res = new ArrayList<U>();
        
        for(var e: src) {
            res.add(mapper.apply(e));
        }
        return res;
    }
    
    public static <T,U> Iterable<U> flatMap(Iterable<T> src,
                              Function<T,Iterable<U>> mapper) {
        var res = new ArrayList<U>();
        
        for(var e: src) {
            for(var u : mapper.apply(e)) {
                res.add(u);
            }
        }
        return res;
    }
  
    // terminal operation
    
    public static <T extends Comparable<T>> T max(Iterable<T> src) {
        Iterator<T> it = src.iterator();
        if (!it.hasNext()) throw new IllegalStateException();
        
        T m = it.next();
        
        while(it.hasNext()) {
            T e = it.next();
            if (e.compareTo(m) > 0) m = e;
        }
        return m;
    }
}
