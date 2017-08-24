package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.ModelCityWeather;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.provider.CallbackCities;
import com.tensor.dapavlov1.tensorfirststep.provider.CallbackCity;

import java.util.List;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public interface CitiesRepository {
    void add(ModelCityWeather city);

    void delete(Object city);

    void getCity(String fullNameCity, CallbackCity<City> callbackCity);

    void getCities(CallbackCities<List<City>> callbackCities);
}
