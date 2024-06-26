package isel.leirt.mpd.weather3.dto;

import static isel.leirt.mpd.weather3.utils.PrintUtils.EOL;

public class WeatherInfoForecastDto extends WeatherInfoBaseDto {
    private String dt_txt;
    private String date_text;

    @Override
    protected String get_formatted_date() {
        if (date_text == null) {
            String parts[] = dt_txt.split(" ");
            date_text = parts[0] + "T" + parts[1];
        }
        return date_text;
    }

    @Override
    public String toString() {
        return "{" + EOL
            + "\tdateTime = " + dateTime() + EOL
            + "\tdescription = " + description() + EOL
            + "\tweather = " + weather() + EOL
            + "}";
    }
}
