package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.ModelCityWeather;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.provider.callbacks.CallbackCities;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.CityFoundException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.CityNotFoundException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyResponseException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.NetworkConnectException;

import io.reactivex.Observable;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public interface CityDataStore {
    void add(ModelCityWeather city);

    void delete(Object city);

    Observable<City> getCity(String fullCityName) throws EmptyResponseException;
}
