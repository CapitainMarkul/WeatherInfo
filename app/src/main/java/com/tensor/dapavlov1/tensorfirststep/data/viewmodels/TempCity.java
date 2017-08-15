package com.tensor.dapavlov1.tensorfirststep.data.viewmodels;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.ModelCityWeather;

/**
 * Created by da.pavlov1 on 15.08.2017.
 */

public class TempCity {
    private static TempCity instance;
    private TempCity(){

    }
    public static TempCity getInstance(){
        if(instance == null){
            instance = new TempCity();
        }
        return instance;
    }
    private ModelCityWeather modelCityWeather;

    public void setModelCityWeather(ModelCityWeather modelCityWeather) {
        this.modelCityWeather = modelCityWeather;
    }

    public ModelCityWeather getModelCityWeather(){
        return modelCityWeather;
    }
}
