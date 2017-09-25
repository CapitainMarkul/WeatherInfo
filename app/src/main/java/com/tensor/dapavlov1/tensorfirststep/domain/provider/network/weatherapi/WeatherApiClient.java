package com.tensor.dapavlov1.tensorfirststep.domain.provider.network.weatherapi;

import android.support.annotation.NonNull;

import com.tensor.dapavlov1.tensorfirststep.domain.provider.network.NetworkCommand;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.network.weatherapi.command.BuildRequestCommand;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.network.weatherapi.command.CallCommand;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by da.pavlov1 on 14.08.2017.
 */

public class WeatherApiClient {
    private final OkHttpClient okHttpClient;

    @Inject
    public WeatherApiClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    //WeatherApiClient
    public Observable<String> getWeatherByCitiesRx(@NonNull List<String> cityNames) {
        return Observable.fromIterable(cityNames).flatMap(city -> getWeatherByCityRx(city));
    }

    public Observable<String> getWeatherByCityRx(@NonNull String fullCityName) {
        NetworkCommand<Observable<String>> callCommand =
                new CallCommand(okHttpClient, buildRequest(fullCityName));
        return callCommand.execute();
    }

    private Request buildRequest(@NonNull String fullCityName) {
        NetworkCommand<Request> buildRequestCommand = new BuildRequestCommand(fullCityName);
        return buildRequestCommand.execute();
    }
}
