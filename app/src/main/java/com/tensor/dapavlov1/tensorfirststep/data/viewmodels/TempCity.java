package com.tensor.dapavlov1.tensorfirststep.data.viewmodels;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.ModelCityWeather;

/**
 * Created by da.pavlov1 on 15.08.2017.
 */

public class TempCity {
    public static TempCity getInstance() {
        return TempCityLoader.INSTANCE;
    }

    private static final class TempCityLoader{
        private static final TempCity INSTANCE = new TempCity();
    }

    private TempCity() {
    }


    private ModelCityWeather modelCityWeather;

    public void setModelCityWeather(ModelCityWeather modelCityWeather) {
        this.modelCityWeather = modelCityWeather;
    }

    public ModelCityWeather getModelCityWeather(){
        return modelCityWeather;
    }
}
