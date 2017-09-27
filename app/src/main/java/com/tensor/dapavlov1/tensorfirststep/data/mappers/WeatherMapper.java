package com.tensor.dapavlov1.tensorfirststep.data.mappers;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.WeatherDb;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.WeatherView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by da.pavlov1 on 26.09.2017.
 */

public class WeatherMapper {
    private WeatherMapper() {
    }

    public static List<WeatherDb> convertVMWeatherToDbWeather(List<WeatherView> weathers) {
        List<WeatherDb> result = new ArrayList<>();
        for (WeatherView item : weathers) {
            result.add(converter(item));
        }
        return result;
    }

    private static WeatherDb converter(WeatherView weather) {
        return new WeatherDb(
                weather.getWindShort(),
                weather.getWindSpeed(),
                weather.getPressure(),
                weather.getTemperature(),
                weather.getDate(),
                weather.getTime(),
                weather.getIconUrl(),
                weather.getIconCode(),
                weather.getDescription());
    }
}