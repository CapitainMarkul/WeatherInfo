package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityWeatherWrapper;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;

import io.reactivex.Flowable;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public interface CitiesDataStore {

    Flowable<CityView> getCitiesRx();

    void add(CityWeatherWrapper city);

    void delete(Object city);
}
