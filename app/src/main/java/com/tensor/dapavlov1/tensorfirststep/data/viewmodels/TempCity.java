package com.tensor.dapavlov1.tensorfirststep.data.viewmodels;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityWeatherWrapper;

/**
 * Created by da.pavlov1 on 15.08.2017.
 */

public class TempCity {
    public static TempCity getInstance() {
        return TempCityLoader.INSTANCE;
    }

    private static final class TempCityLoader {
        private static final TempCity INSTANCE = new TempCity();
    }

    private TempCity() {
    }

    private CityWeatherWrapper cityWeatherWrapper;

    public void setCityWeatherWrapper(CityWeatherWrapper cityWeatherWrapper) {
        this.cityWeatherWrapper = cityWeatherWrapper;
    }

    public CityWeatherWrapper getCityWeatherWrapper() {
        return cityWeatherWrapper;
    }
}
