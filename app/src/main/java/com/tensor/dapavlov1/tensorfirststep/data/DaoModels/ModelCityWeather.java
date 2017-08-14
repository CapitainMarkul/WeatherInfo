package com.tensor.dapavlov1.tensorfirststep.data.DaoModels;

import java.util.List;

/**
 * Created by da.pavlov1 on 07.08.2017.
 */

//Класс обертка
public class ModelCityWeather {
    private DaoCity daoCity;
    private List<DaoWeather> weathers;

    public ModelCityWeather(DaoCity daoCity, List<DaoWeather> weathers) {
        this.daoCity = daoCity;
        this.weathers = weathers;
    }

    public DaoCity getDaoCity() {
        return daoCity;
    }

    public List<DaoWeather> getWeathers() {
        return weathers;
    }
}
