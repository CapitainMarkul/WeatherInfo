package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.provider.callbacks.CallbackCities;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyDbException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.NetworkConnectException;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public interface CitiesDataStore {
    //    List<City> getCities(List<City> citiesFromDb);
//    List<City> getCities() throws NetworkConnectException, EmptyDbException;
//    List<City> getCities(CallbackCities<List<City>> callbackCities) throws NetworkConnectException, EmptyDbException;
//    void getCities(CallbackCities<City> callbackCities) throws NetworkConnectException, EmptyDbException;

    Flowable<City> getCitiesRx();

//    Maybe<List<City>> getCitiesRx() throws NetworkConnectException, EmptyDbException;
}
