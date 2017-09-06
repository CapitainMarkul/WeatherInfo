package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;

import io.reactivex.Flowable;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public interface CitiesDataStore {
    //    List<CityView> getCities(List<CityView> citiesFromDb);
//    List<CityView> getCities() throws NetworkConnectException, EmptyDbException;
//    List<CityView> getCities(CitiesCallback<List<CityView>> callbackCities) throws NetworkConnectException, EmptyDbException;
//    void getCities(CitiesCallback<CityView> callbackCities) throws NetworkConnectException, EmptyDbException;

    Flowable<CityView> getCitiesRx();

//    Maybe<List<CityView>> getCitiesRx() throws NetworkConnectException, EmptyDbException;
}
