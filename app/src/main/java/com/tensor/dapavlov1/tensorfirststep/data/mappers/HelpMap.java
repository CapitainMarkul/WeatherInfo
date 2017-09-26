package com.tensor.dapavlov1.tensorfirststep.data.mappers;

import com.tensor.dapavlov1.tensorfirststep.BuildConfig;
import com.tensor.dapavlov1.tensorfirststep.data.gsonmodels.WeatherRootGson;

/**
 * Created by da.pavlov1 on 11.08.2017.
 */

public abstract class HelpMap {
    protected static String getIconUrlLoad(WeatherRootGson weatherRootGson) {
        return BuildConfig.WEATHER_API_URL_TO_LOAD_IMAGE + weatherRootGson.getWeatherChildGson().getIcon() + BuildConfig.WEATHER_API_EXPANSION_LOAD_IMAGE;
    }

    protected static String getIconCode(WeatherRootGson weatherRootGson) {
        return weatherRootGson.getWeatherChildGson().getIcon();
    }

    protected static String getWeatherDescription(WeatherRootGson weatherRootGson) {
        return weatherRootGson.getWeatherChildGson().getDescription();
    }
}
