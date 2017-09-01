package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.ModelCityWeather;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.provider.callbacks.CallbackCities;
import com.tensor.dapavlov1.tensorfirststep.provider.callbacks.CallbackCity;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyResponseException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.NetworkConnectException;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public interface CitiesRepository {
    void add(ModelCityWeather city);

    void delete(Object city);

    void getCity(String fullNameCity, CallbackCity<City> callbackCities) throws EmptyResponseException, NetworkConnectException;

    Flowable<City> getCitiesRx();
//    Observable<String> getCity(String fullCityName) throws NetworkConnectException;

//    void getCities(CallbackCities<City> callbackCities);
}
