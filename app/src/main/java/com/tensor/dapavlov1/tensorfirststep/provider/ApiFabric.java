package com.tensor.dapavlov1.tensorfirststep.provider;

import com.tensor.dapavlov1.tensorfirststep.provider.client.GoogleApiClient;
import com.tensor.dapavlov1.tensorfirststep.provider.client.WeatherApiClient;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class ApiFabric {
    public static ApiFabric getInstance() {
        return ApiFabricLoader.INSTANCE;
    }

    private static final class ApiFabricLoader {
        private static final ApiFabric INSTANCE = new ApiFabric();
    }

    private ApiFabric() {
    }
//    private volatile ApiFabric instance;
//
    //
//    public ApiFabric getInstance() {
//        ApiFabric localInstance = instance;
//        if (localInstance == null) {
//            synchronized (this) {
//                localInstance = instance;
//                if (localInstance == null) {
//                    instance = localInstance = new ApiFabric();
//                }
//                return instance;
//            }
//        }
//        return localInstance;
//    }

    public WeatherApiClient createClientWeatherApi() {
        return new WeatherApiClient();
    }

    public GoogleApiClient crateClientGoogleApi() {
        return new GoogleApiClient();
    }
}
