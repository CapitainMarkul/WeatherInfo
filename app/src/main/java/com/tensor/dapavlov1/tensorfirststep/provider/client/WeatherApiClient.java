package com.tensor.dapavlov1.tensorfirststep.provider.client;

import android.support.annotation.NonNull;
import android.util.Log;

import com.tensor.dapavlov1.tensorfirststep.BuildConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by da.pavlov1 on 14.08.2017.
 */

public class WeatherApiClient extends ApiHelper {
    private OkHttpClient okHttpClient;

    public WeatherApiClient() {
        okHttpClient = new OkHttpClient();
    }

    private Map<String, String> createMapForWeatherApi(String cityName, String key, String localisation, int countDays) {
        Map<String, String> map = new HashMap<>();
        map.put("city", cityName);
        map.put("key", key);
        map.put("lang", localisation);
        map.put("days", String.valueOf(countDays));
        return map;
    }

    private Request createRequest(@NonNull String cityName) {
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

    public Observable<String> getWeatherInCityRx(@NonNull String cityName) {
        return Observable.create(source -> {
            Call call = okHttpClient.newCall(createRequest(cityName));

            //отменяем запрос, если произошла отписка
            source.setCancellable(call::cancel);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    source.setCancellable(call::cancel);
                    Log.e("call:", "cancel");
//                    source.onError(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    source.onNext(response.body().string());
                    source.onComplete();
                }
            });
        });
    }

    //WeatherApiClient
    public Observable<String> getWeatherInCityRx(@NonNull List<String> cityNames) {
        return Observable.create(source -> {
            for (String cityName : cityNames) {
                Call call = okHttpClient.newCall(createRequest(cityName));
                //отменяем запрос, если произошла отписка
//                source.setCancellable(call::cancel);

                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        source.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        source.onNext(response.body().string());
//                        source.onComplete();
                    }
                });
            }

            // TODO: 31.08.2017 В какой момент отправлять onComplete ?
            // Если отправить отсюда, то данные не успевают отобразиться
//            source.onComplete();
        });
    }
}
