package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.provider.callbacks.CityCallback;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyDbException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyResponseException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.NetworkConnectException;

import io.reactivex.Flowable;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public interface CitiesRepository {
    void add(CityView city);

    void delete(String cityName);

    void getCity(String fullNameCity, CityCallback<CityView> callbackCities);

    Flowable<CityView> getCitiesRx() throws EmptyDbException;
}
