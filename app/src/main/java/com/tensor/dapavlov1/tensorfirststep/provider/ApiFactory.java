package com.tensor.dapavlov1.tensorfirststep.provider;

import com.tensor.dapavlov1.tensorfirststep.provider.client.GoogleApiClient;
import com.tensor.dapavlov1.tensorfirststep.provider.client.WeatherApiClient;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class ApiFactory {
    public static ApiFactory getInstance() {
        return ApiFabricLoader.INSTANCE;
    }

    private static final class ApiFabricLoader {
        private static final ApiFactory INSTANCE = new ApiFactory();
    }

    private ApiFactory() {
    }
//    private volatile ApiFactory instance;
//
    //
//    public ApiFactory getInstance() {
//        ApiFactory localInstance = instance;
//        if (localInstance == null) {
//            synchronized (this) {
//                localInstance = instance;
//                if (localInstance == null) {
//                    instance = localInstance = new ApiFactory();
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
