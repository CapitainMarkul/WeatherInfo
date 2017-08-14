package com.tensor.dapavlov1.tensorfirststep;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class ConnectToApi {
    private static ConnectToApi instance;

    private ConnectToApi() {
        okHttpClient = new OkHttpClient();
    }

    public static ConnectToApi getInstance() {
        if (instance == null) {
            instance = new ConnectToApi();
        }
        return instance;
    }

    private OkHttpClient okHttpClient;

    @Nullable
    public String getJsonFromApiWeather(@NotNull Map<String, String> map) throws IOException {
        Request request = new Request.Builder().url(
                createBuilder(
                        BuildConfig.WEATHER_API_SCHEME,
                        BuildConfig.WEATHER_API_HOST,
                        BuildConfig.WEATHER_API_SEGMENTS, map).build()).build();
        Response response = okHttpClient.newCall(request).execute();

        return response.body().string();
    }

    @Nullable
    public String getJsonFromGooglePlaceApi(@NotNull Map<String, String> map) throws IOException {
        Request request = new Request.Builder().url(
                createBuilder(
                        BuildConfig.GOOGLE_API_SCHEME,
                        BuildConfig.GOOGLE_API_HOST,
                        BuildConfig.GOOGLE_API_SEGMENTS,
                        map).build()).build();
        Response response = okHttpClient.newCall(request).execute();

        return response.body().string();
    }

    private HttpUrl.Builder createBuilder(String scheme, String host, String segment, @NotNull Map<String, String> map) {
        HttpUrl.Builder builder = new HttpUrl.Builder()
                .scheme(scheme)
                .host(host)
                .addPathSegments(segment);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.addQueryParameter(entry.getKey(), entry.getValue());
        }
        return builder;
    }

    public Map<String, String> createMapForWeatherApi(String cityName, String key, String localisation, int countDays) {
        Map<String, String> map = new HashMap<>();
        map.put("city", cityName);
        map.put("key", key);
        map.put("lang", localisation);
        map.put("days", String.valueOf(countDays));
        return map;
    }

    public Map<String, String> createMapForGoogleApi(String key, String types, String input) {
        Map<String, String> map = new HashMap<>();
        map.put("types", types);
        map.put("key", key);
        map.put("input", input);
        return map;
    }
}
