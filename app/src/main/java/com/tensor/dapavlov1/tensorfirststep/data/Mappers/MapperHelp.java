package com.tensor.dapavlov1.tensorfirststep.data.Mappers;

import com.tensor.dapavlov1.tensorfirststep.BuildConfig;
import com.tensor.dapavlov1.tensorfirststep.data.GsonModels.GsonWeatherRoot;

/**
 * Created by da.pavlov1 on 11.08.2017.
 */

public abstract class MapperHelp {
    protected String getIconUrlLoad(GsonWeatherRoot gsonWeatherRoot) {
        return BuildConfig.WEATHER_API_URL_TO_LOAD_IMAGE + gsonWeatherRoot.getGsonWeatherChild().getIcon() + BuildConfig.WEATHER_API_EXPANSION_LOAD_IMAGE;
    }

    protected String getIconCode(GsonWeatherRoot gsonWeatherRoot) {
        return gsonWeatherRoot.getGsonWeatherChild().getIcon();
    }

    protected String getWeatherDescription(GsonWeatherRoot gsonWeatherRoot) {
        return gsonWeatherRoot.getGsonWeatherChild().getDescription();
    }
}
