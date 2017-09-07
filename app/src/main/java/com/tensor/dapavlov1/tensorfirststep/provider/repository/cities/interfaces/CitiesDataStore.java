package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.WeatherDb;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public interface CitiesDataStore {

    Flowable<CityView> getCitiesRx();

    void add(String cityName, String lastTimeUpdate, List<WeatherDb> weathers);

    void delete(String cityName);
}
