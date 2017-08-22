package com.tensor.dapavlov1.tensorfirststep.provider;

import com.tensor.dapavlov1.tensorfirststep.provider.client.GoogleApi;
import com.tensor.dapavlov1.tensorfirststep.provider.client.WeatherApi;

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

    WeatherApi createClientWeatherApi() {
        return new WeatherApi();
    }

    GoogleApi crateClientGoogleApi() {
        return new GoogleApi();
    }
}
