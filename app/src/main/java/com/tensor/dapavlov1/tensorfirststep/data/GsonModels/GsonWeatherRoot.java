package com.tensor.dapavlov1.tensorfirststep.data.GsonModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by da.pavlov1 on 11.08.2017.
 */

public class GsonWeatherRoot {
    @SerializedName("wind_cdir") String windShort;
    @SerializedName("wind_spd") double windSpeed;
    @SerializedName("pres") double pressure;
    @SerializedName("temp") double temperature;
    @SerializedName("datetime") String date;
    @SerializedName("weather") GsonWeatherChild gsonWeatherChild;

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

    public GsonWeatherChild getGsonWeatherChild() {
        return gsonWeatherChild;
    }
}
