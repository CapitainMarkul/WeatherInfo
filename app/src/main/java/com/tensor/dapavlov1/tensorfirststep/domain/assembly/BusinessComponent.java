package com.tensor.dapavlov1.tensorfirststep.domain.assembly;

import com.tensor.dapavlov1.tensorfirststep.core.CoreComponent;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.service.WeatherService;

import dagger.Component;

/**
 * Created by da.pavlov1 on 21.09.2017.
 */

@PerBusinessScope
@Component(modules = BusinessModule.class, dependencies = CoreComponent.class)
public interface BusinessComponent {
    void inject(WeatherService weatherService);

    WeatherService getWeatherService();
}
