package com.tensor.dapavlov1.tensorfirststep.data.mappers;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DbCity;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DbWeather;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.ModelCityWeather;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.Weather;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by da.pavlov1 on 10.08.2017.
 */

public class MapperDbToView {
    public static MapperDbToView getInstance() {
        return MapperDbToViewLoader.INSTANCE;
    }

    private static final class MapperDbToViewLoader {
        private static final MapperDbToView INSTANCE = new MapperDbToView();
    }

    private MapperDbToView() {
    }

    public City convertDbModelToViewModel(DbCity dbCity, List<DbWeather> dbWeather) {
        return new City(dbCity.getName(), dbCity.getLastTimeUpdate(), getWeathers(dbWeather));
    }

    public List<City> getCityViewModels(List<ModelCityWeather> dbCity) {
        List<City> cityList = new ArrayList<>();
        for (ModelCityWeather item : dbCity) {
            cityList.add(
                    convertDbModelToViewModel(
                            item.getDbCity().getName(),
                            item.getDbCity().getLastTimeUpdate(),
                            item.getWeathers()
                    )
            );
        }
        return cityList;
    }

    public List<City> getCityViewModelsFromDao(List<DbCity> dbCity) {
        List<City> cityList = new ArrayList<>();
        for (DbCity item : dbCity) {
            cityList.add(
                    convertDbModelToViewModel(
                            item.getName(),
                            item.getLastTimeUpdate(),
                            item.getWeathers()
                    )
            );
        }
        return cityList;
    }

    private City convertDbModelToViewModel(String cityName, String lastTimeUpdate, List<DbWeather> weathers) {
        return new City(cityName, lastTimeUpdate, getWeathers(weathers));
    }

    private List<Weather> getWeathers(List<DbWeather> dbWeather) {
        List<Weather> weathers = new ArrayList<>();
        for (DbWeather itemBd : dbWeather) {
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
