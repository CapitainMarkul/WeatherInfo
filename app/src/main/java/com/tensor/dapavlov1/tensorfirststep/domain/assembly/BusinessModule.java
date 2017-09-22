package com.tensor.dapavlov1.tensorfirststep.domain.assembly;

import com.tensor.dapavlov1.tensorfirststep.domain.provider.service.WeatherService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by da.pavlov1 on 21.09.2017.
 */

@Module
public class BusinessModule {

    @Provides
    @PerBusinessScope
    WeatherService provideWeatherService() {
        return new WeatherService();
    }
}
