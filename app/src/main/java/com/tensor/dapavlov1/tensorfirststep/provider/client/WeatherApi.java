package com.tensor.dapavlov1.tensorfirststep.provider.client;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tensor.dapavlov1.tensorfirststep.BuildConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by da.pavlov1 on 14.08.2017.
 */

public class WeatherApi extends ApiHelper {
    private OkHttpClient okHttpClient;

    public WeatherApi() {
        okHttpClient = new OkHttpClient();
    }

    @Nullable
    public String getJsonFromApiWeather(@NonNull String cityName) throws IOException {
        Request request = new Request.Builder().url(
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

        Response response = okHttpClient.newCall(request).execute();

        return response.body().string();
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
