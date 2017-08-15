package com.tensor.dapavlov1.tensorfirststep.provider;

import com.tensor.dapavlov1.tensorfirststep.provider.client.GoogleApi;
import com.tensor.dapavlov1.tensorfirststep.provider.client.WeatherApi;

import java.io.IOException;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class ApiFabrica {
    private static ApiFabrica instance;

    private ApiFabrica() {
    }

    public static ApiFabrica getInstance() {
        if (instance == null) {
            instance = new ApiFabrica();
        }
        return instance;
    }

    public WeatherApi createClientWeatherApi() {
        return new WeatherApi();
    }

    public GoogleApi crateClientGoogleApi() {
        return new GoogleApi();
    }

//    public Map<String, String> createMapForGoogleApi(String key, String types, String input) {
//        Map<String, String> map = new HashMap<>();
//        map.put("types", types);
//        map.put("key", key);
//        map.put("input", input);
//        return map;
//    }

//    @Nullable
//    public String getJsonFromApiWeather(@NotNull Map<String, String> map) throws IOException {
//        Request request = new Request.Builder().url(
//                createUrl(
//                        BuildConfig.WEATHER_API_SCHEME,
//                        BuildConfig.WEATHER_API_HOST,
//                        BuildConfig.WEATHER_API_SEGMENTS,
//                        map)).build();
//        Response response = okHttpClient.newCall(request).execute();
//
//        return response.body().string();
//    }

//    @Nullable
//    public String getJsonFromGooglePlaceApi(@NotNull Map<String, String> map) throws IOException {
//        Request request = new Request.Builder().url(
//                createUrl(
//                        BuildConfig.GOOGLE_API_SCHEME,
//                        BuildConfig.GOOGLE_API_HOST,
//                        BuildConfig.GOOGLE_API_SEGMENTS,
//                        map)).build();
//        Response response = okHttpClient.newCall(request).execute();
//
//        return response.body().string();
//    }

//    private String createUrl(String scheme, String host, String segment, @NotNull Map<String, String> map) {
//        HttpUrl.Builder builder = new HttpUrl.Builder()
//                .scheme(scheme)
//                .host(host)
//                .addPathSegments(segment);
//
//        for (Map.Entry<String, String> entry : map.entrySet()) {
//            builder.addQueryParameter(entry.getKey(), entry.getValue());
//        }
//        return builder.toString();
//    }

//    public Map<String, String> createMapForWeatherApi(String cityName, String key, String localisation, int countDays) {
//        Map<String, String> map = new HashMap<>();
//        map.put("city", cityName);
//        map.put("key", key);
//        map.put("lang", localisation);
//        map.put("days", String.valueOf(countDays));
//        return map;
//    }

//    public Map<String, String> createMapForGoogleApi(String key, String types, String input) {
//        Map<String, String> map = new HashMap<>();
//        map.put("types", types);
//        map.put("key", key);
//        map.put("input", input);
//        return map;
//    }
}
