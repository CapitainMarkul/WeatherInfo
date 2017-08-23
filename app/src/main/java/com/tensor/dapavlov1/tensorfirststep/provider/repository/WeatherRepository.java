package com.tensor.dapavlov1.tensorfirststep.provider.repository;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.provider.CallbackCities;

import java.util.List;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public interface WeatherRepository {
    City city();

    void getCities(CallbackCities<List<City>> callbackCities);
}
