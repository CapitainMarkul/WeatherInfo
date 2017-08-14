package com.tensor.dapavlov1.tensorfirststep.Mappers;

import com.tensor.dapavlov1.tensorfirststep.data.DaoModels.DaoCity;
import com.tensor.dapavlov1.tensorfirststep.data.DaoModels.DaoWeather;
import com.tensor.dapavlov1.tensorfirststep.data.ViewModels.City;
import com.tensor.dapavlov1.tensorfirststep.data.ViewModels.Weather;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by da.pavlov1 on 10.08.2017.
 */

public class MapperDbToView {
    private static MapperDbToView instance;

    private MapperDbToView() {
    }

    public static MapperDbToView getInstance() {
        if (instance == null) {
            instance = new MapperDbToView();
        }
        return instance;
    }

    public City convertDbModelToViewModel(DaoCity dbCity, List<DaoWeather> dbWeather) {
        return new City(dbCity.getName(), dbCity.getLastTimeUpdate(), getWeathers(dbWeather));
    }

    public List<City> getCityViewModels(List<DaoCity> dbCity) {
        List<City> cityList = new ArrayList<>();
        for (DaoCity itemDb : dbCity) {
            cityList.add(
                    convertDbModelToViewModel(
                            itemDb.getName(),
                            itemDb.getLastTimeUpdate(),
                            itemDb.getWeathers()
                    )
            );
        }
        return cityList;
    }

    private City convertDbModelToViewModel(String cityName, String lastTimeUpdate, List<DaoWeather> weathers){
        return new City(cityName, lastTimeUpdate, getWeathers(weathers));
    }

    private List<Weather> getWeathers(List<DaoWeather> dbWeather) {
        List<Weather> weathers = new ArrayList<>();
        for (DaoWeather itemBd : dbWeather) {
            weathers.add(new Weather(
                    itemBd.getWindShort(),
                    itemBd.getWindSpeed(),
                    itemBd.getPressure(),
                    itemBd.getTemperature(),
                    itemBd.getDate(),
                    itemBd.getTime(),
                    itemBd.getIconUrl(),
                    itemBd.getIconCode(),
                    itemBd.getDescription()
            ));
        }
        return weathers;
    }
}
