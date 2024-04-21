package isel.leirt.mpd.weather3;

import com.google.gson.Gson;
import isel.leirt.mpd.weather3.dto.*;
import isel.leirt.mpd.weather3.exceptions.WeatherApiException;
import isel.leirt.mpd.weather3.requests.*;
import isel.leirt.mpd.weather3.utils.TimeUtils;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


public class OpenWeatherWebApi {
    private static final String API_KEY;

    private static final String WEATHER_HOST =
            "http://api.openweathermap.org/";

    private static final String GEO_SERVICE =
            "geo/1.0/";

    private static final String WEATHER_SERVICE =
            WEATHER_HOST + "data/2.5/";

    private static final String WEATHER_AT_TEMPLATE =
            "weather?lat=%f&lon=%f&units=metric&appid=%s";
    
    private static final String FORECAST_WEATHER_TEMPLATE =
            "forecast?lat=%f&lon=%f&units=metric&appid=%s";

    private static final String AIR_POLLUTION_AT_TEMPLATE =
            "air_pollution?lat=%f&lon=%f&appid=%s";

    private static final String AIR_POLLUTION_HISTORY_TEMPLATE =
            "air_pollution/history?lat=%f&lon=%f&start=%d&end=%d&appid=%s";

    private static final String LOCATION_SEARCH_TEMPLATE =
        WEATHER_HOST +
        GEO_SERVICE +
        "direct?q=%s&limit=10&lang=pt&appid=%s";

    protected final Gson gson;
    private final Request req;
    
    
    /**
     * Retrieve API-KEY from resources
     * @return
     */
    private static String getApiKeyFromResources() {
        try {
            URL keyFile =
                    ClassLoader.getSystemResource("openweatherapi-app-key.txt");
            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(keyFile.openStream()))) {
                return reader.readLine();
            }

        }
        catch(IOException e) {
            throw new IllegalStateException(
                    "YOU MUST GET a KEY from  openweatherapi.com and place it in src/main/resources/openweatherapi-app-key2.txt");
        }
    }
    
    /**
     * Static Constructor
     */
    static {
        API_KEY = getApiKeyFromResources();
    }

    // API METHODS
    
    /**
     * Get WeatherInfo's from a local coordinates given a date interval
     * @param lat
     * @param lon
     * @return
     */
    public WeatherInfoDto weatherAt(double lat, double lon) {
        String path =  WEATHER_SERVICE +
            String.format(WEATHER_AT_TEMPLATE, lat, lon, API_KEY);
      
        try (Reader reader = req.get(path)) {
            WeatherInfoDto winfo =
                    gson.fromJson(reader, WeatherInfoDto.class);
            return winfo;
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    /**
     * Auxiliary method to save a request response in a local cache
     * In the second grade you will do this in sa different way, without the need
     * for this class changes
     * @param lat
     * @param lon
     */
    public void saveWeatherAt(double lat, double lon) {
        String path =  WEATHER_SERVICE +
                           String.format(WEATHER_AT_TEMPLATE, lat, lon, API_KEY);
        
        try (Reader reader = req.get(path)) {
            MockRequest.saveOn(path, reader);
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Get current air pollution metrics from a local coordinates
     * @param lat
     * @param lon
     * @return
     */
    public PollutionInfoDto airPollutionAt(double lat, double lon) {
        String path =   WEATHER_SERVICE +
            String.format(AIR_POLLUTION_AT_TEMPLATE, lat, lon, API_KEY);
 
        try(Reader reader = req.get(path)) {
            PollutionInfoQueryDto pi =
                gson.fromJson(reader, PollutionInfoQueryDto.class);
            if (pi.list == null || pi.list.length != 1)
                throw new WeatherApiException("response list must have one element");
            return pi.list[0];
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    /**
     * Get WeatherInfo's forecast for a local coordinates
     * @param lat
     * @param lon
     * @return
     */
    public List<WeatherInfoForecastDto> forecastWeatherAt(double lat, double lon) {
        String path =  WEATHER_SERVICE + String.format(FORECAST_WEATHER_TEMPLATE, lat, lon, API_KEY);
    
        try (Reader reader = req.get(path)) {
            ForecastInfoDto finfo =
                    gson.fromJson(reader, ForecastInfoDto.class);
            var local = finfo.getLocal();
            return finfo.getForecast();
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }

   
    /**
     * Get local info given the name of the local
     * @param location
     * @return
     */
    public List<LocationDto> search(String location) {
       
        String path =  String.format(LOCATION_SEARCH_TEMPLATE, location, API_KEY);
       
        try (Reader reader = req.get(path)) {
            LocationDto[] search = gson.fromJson(reader, LocationDto[].class);
            return Arrays.asList(search);
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public List<PollutionInfoDto> pollutionHistoryAt(
            double lati, double longi, LocalDate start, LocalDate end) {

        String path =  WEATHER_SERVICE +
            String.format(AIR_POLLUTION_HISTORY_TEMPLATE,
                            lati, longi,
                    TimeUtils.toUnixTime(start), TimeUtils.toUnixTime(end), API_KEY);
        
        try (Reader reader = req.get(path)) {
            PollutionInfoQueryDto winfo =
                    gson.fromJson(reader, PollutionInfoQueryDto.class);
            return Arrays.asList(winfo.list);
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public OpenWeatherWebApi(Request req) {
        this.req = req;
        gson = new Gson();
    }

    public OpenWeatherWebApi() {
        this(new HttpRequest());
    }
}
