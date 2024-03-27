package isel.leirt.mpd.weather.functional;

import isel.leirt.mpd.weather.dto.WeatherInfoForecastDto;

// a predicate just for WeatherInfoForecast objects
// not very usefull!
public interface WeatherInfoForecastPredicate {
    boolean test(WeatherInfoForecastDto wi);
}
