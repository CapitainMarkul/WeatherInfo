package com.tensor.dapavlov1.tensorfirststep.domain.provider.service;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.client.DbClient;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.client.GoogleApiClient;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.client.WeatherApiClient;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * Created by da.pavlov1 on 21.09.2017.
 */

public class WeatherService {
    @Inject
    Lazy<WeatherApiClient> weatherApiService;

    @Inject
    Lazy<GoogleApiClient> googleApiService;

    @Inject
    Lazy<DbClient> dbService;

    public WeatherService() {
        App.get().businessComponent().inject(this);
    }

    public WeatherApiClient getWeatherService() {
        return weatherApiService.get();
    }

    public GoogleApiClient getGoogleApiService() {
        return googleApiService.get();
    }

    public DbClient getDbService() {
        return dbService.get();
    }
}
