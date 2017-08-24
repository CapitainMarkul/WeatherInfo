package com.tensor.dapavlov1.tensorfirststep.provider;

import com.tensor.dapavlov1.tensorfirststep.provider.client.GoogleApiClient;
import com.tensor.dapavlov1.tensorfirststep.provider.client.WeatherApiClient;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class ApiFabric {
    private static ApiFabric instance;

    private ApiFabric() {
    }

    public static ApiFabric getInstance() {
        if (instance == null) {
            instance = new ApiFabric();
        }
        return instance;
    }

    public WeatherApiClient createClientWeatherApi() {
        return new WeatherApiClient();
    }

    public GoogleApiClient crateClientGoogleApi() {
        return new GoogleApiClient();
    }
}
