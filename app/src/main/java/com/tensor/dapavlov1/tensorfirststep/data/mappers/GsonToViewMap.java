package com.tensor.dapavlov1.tensorfirststep.data.mappers;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.WeatherView;
import com.tensor.dapavlov1.tensorfirststep.provider.common.TrimDateSingleton;
import com.tensor.dapavlov1.tensorfirststep.data.gsonmodels.WeatherRootGson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by da.pavlov1 on 10.08.2017.
 */

public class GsonToViewMap extends HelpMap {
    public static GsonToViewMap getInstance() {
        return MapperGsonToViewLoader.INSTANCE;
    }

    private static final class MapperGsonToViewLoader{
        private static final GsonToViewMap INSTANCE = new GsonToViewMap();
    }

    private GsonToViewMap() {
    }

    private List<WeatherView> getWeathersFromGson(List<WeatherRootGson> gsonWeathers) {
        List<WeatherView> viewWeatherViews = new ArrayList<>();
        for (WeatherRootGson itemRoot : gsonWeathers) {
            viewWeatherViews.add(
                    new WeatherView(
                            itemRoot.getWindShort(),
                            itemRoot.getWindSpeed(),
                            itemRoot.getPressure(),
                            itemRoot.getTemperature(),
                            TrimDateSingleton.getInstance().trimDate(itemRoot.getDate()),
                            TrimDateSingleton.getInstance().trimTime(itemRoot.getDate()),
                            getIconUrlLoad(itemRoot),
                            getIconCode(itemRoot),
                            getWeatherDescription(itemRoot)
                    )
            );
        }
        return viewWeatherViews;
    }
}
