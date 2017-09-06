package com.tensor.dapavlov1.tensorfirststep.data.mappers;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDb;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.WeatherDb;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityWeatherWrapper;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.WeatherView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by da.pavlov1 on 10.08.2017.
 */

public class DbToViewMap {
    public static DbToViewMap getInstance() {
        return MapperDbToViewLoader.INSTANCE;
    }

    private static final class MapperDbToViewLoader {
        private static final DbToViewMap INSTANCE = new DbToViewMap();
    }

    private DbToViewMap() {
    }

    public CityView convertDbModelToViewModel(CityDb cityDb, List<WeatherDb> weatherDb, boolean isFavorite) {
        return new CityView(cityDb.getName(), cityDb.getLastTimeUpdate(), getWeathers(weatherDb), isFavorite);
    }

    public List<CityView> getCityViewModels(List<CityWeatherWrapper> dbCity) {
        List<CityView> cityViewList = new ArrayList<>();
        boolean isFavorite = true;
        for (CityWeatherWrapper item : dbCity) {
            cityViewList.add(
                    convertDbModelToViewModel(
                            item.getCityDb().getName(),
                            item.getCityDb().getLastTimeUpdate(),
                            item.getWeathers(),
                            isFavorite
                    )
            );
        }
        return cityViewList;
    }

    public List<CityView> getCityViewModelsFromDao(List<CityDb> cityDb) {
        List<CityView> cityViewList = new ArrayList<>();
        boolean isFavorite = true;
        for (CityDb item : cityDb) {
            cityViewList.add(
                    convertDbModelToViewModel(
                            item.getName(),
                            item.getLastTimeUpdate(),
                            item.getWeathers(),
                            isFavorite
                    )
            );
        }
        return cityViewList;
    }

    private CityView convertDbModelToViewModel(String cityName, String lastTimeUpdate, List<WeatherDb> weathers, boolean isFavorite) {
        return new CityView(cityName, lastTimeUpdate, getWeathers(weathers), isFavorite);
    }

    private List<WeatherView> getWeathers(List<WeatherDb> weatherDb) {
        List<WeatherView> weatherViews = new ArrayList<>();
        for (WeatherDb itemBd : weatherDb) {
            weatherViews.add(new WeatherView(
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
        return weatherViews;
    }
}
