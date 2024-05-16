package isel.leirt.mpd.weather4.dto;

import isel.leirt.mpd.weather4.utils.PrintUtils;

public class WeatherInfoDto extends WeatherInfoBaseDto {

    private String name;
    private  int timezone;

    public String local() {
        return name;
    }

    @Override
    public String toString() {
        return "{" + PrintUtils.EOL
               + "\tdateTime = " + dateTime() + PrintUtils.EOL
               + "\tdescription = " + description() + PrintUtils.EOL
               + "\tweather = " + weather() + PrintUtils.EOL
               + "\tname = " + local() + PrintUtils.EOL
               + "\ttimezone = " + timezone + PrintUtils.EOL
               + "}";
    }
}
