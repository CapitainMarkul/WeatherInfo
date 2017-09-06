package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityWeatherWrapper;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyResponseException;

import io.reactivex.Observable;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public interface CityDataStore {
    void add(CityWeatherWrapper city);

    void delete(Object city);

    Observable<CityView> getCity(String fullCityName) throws EmptyResponseException;
}
