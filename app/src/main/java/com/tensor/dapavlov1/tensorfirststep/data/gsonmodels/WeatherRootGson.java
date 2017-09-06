package com.tensor.dapavlov1.tensorfirststep.data.gsonmodels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by da.pavlov1 on 11.08.2017.
 */

public class WeatherRootGson {
    @SerializedName("wind_cdir") String windShort;
    @SerializedName("wind_spd") double windSpeed;
    @SerializedName("pres") double pressure;
    @SerializedName("temp") double temperature;
    @SerializedName("datetime") String date;
    @SerializedName("weather") WeatherChildGson weatherChildGson;

    public String getWindShort() {
        return windShort;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getPressure() {
        return pressure;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getDate() {
        return date;
    }

    public WeatherChildGson getWeatherChildGson() {
        return weatherChildGson;
    }
}
