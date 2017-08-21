package com.tensor.dapavlov1.tensorfirststep.data.daomodels;

import java.util.List;

/**
 * Created by da.pavlov1 on 07.08.2017.
 */

//Класс обертка
public class ModelCityWeather {
    private DbCity dbCity;
    private List<DbWeather> weathers;

    public ModelCityWeather(DbCity dbCity, List<DbWeather> weathers) {
        this.dbCity = dbCity;
        this.weathers = weathers;
    }

    public DbCity getDbCity() {
        return dbCity;
    }

    public List<DbWeather> getWeathers() {
        return weathers;
    }
}
