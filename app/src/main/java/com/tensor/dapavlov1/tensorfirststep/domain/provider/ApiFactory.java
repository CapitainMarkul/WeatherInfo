package com.tensor.dapavlov1.tensorfirststep.domain.provider;

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

//    public WeatherApiClient createClientWeatherApi() {
//        return new WeatherApiClient();
//    }
//
//    public GoogleApiClient crateClientGoogleApi() {
//        return new GoogleApiClient();
//    }
}
