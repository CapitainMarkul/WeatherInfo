package com.tensor.dapavlov1.tensorfirststep.domain.provider.network.weatherapi.command;

import com.tensor.dapavlov1.tensorfirststep.BuildConfig;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.network.NetworkUtils;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.network.NetworkCommand;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by da.pavlov1 on 25.09.2017.
 */

public class BuildRequestCommand extends NetworkUtils
        implements NetworkCommand<Request> {
    private final String cityName;

    public BuildRequestCommand(String cityName) {
        this.cityName = trimCityName(cityName);
    }

    @Override
    public Request execute() {
        return new Request.Builder().url(
                createUrl(
                        BuildConfig.WEATHER_API_SCHEME,
                        BuildConfig.WEATHER_API_HOST,
                        BuildConfig.WEATHER_API_SEGMENTS,
                        createMapForWeatherApi(
                                cityName,
                                BuildConfig.WEATHER_API_KEY,
                                BuildConfig.WEATHER_API_LANGUAGE,
                                BuildConfig.WEATHER_API_COUNT_DAY
                        ))).build();
    }

    private Map<String, String> createMapForWeatherApi(String cityName, String key, String localisation, int countDays) {
        Map<String, String> map = new HashMap<>();
        map.put("city", cityName);
        map.put("key", key);
        map.put("lang", localisation);
        map.put("days", String.valueOf(countDays));
        return map;
    }
}
