package com.tensor.dapavlov1.tensorfirststep.provider.repository;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.provider.DataProvider;

import java.util.List;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public class WeatherDataRepository implements WeatherRepository {
    //// TODO: 22.08.2017 Здесь DataProvider, который будет решать, что и как предоставлять

    private DataProvider dataProvider;


    @Override
    public City city() {
        return null;
    }

    @Override
    public List<City> cities() {

        return null;
    }
}
