package com.tensor.dapavlov1.tensorfirststep.data.mappers;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDb;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.WeatherDb;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.common.TrimDateSingleton;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityWeatherWrapper;
import com.tensor.dapavlov1.tensorfirststep.data.gsonmodels.CityGson;
import com.tensor.dapavlov1.tensorfirststep.data.gsonmodels.WeatherRootGson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by da.pavlov1 on 10.08.2017.
 */

public class GsonToDbMap extends HelpMap {
    public static GsonToDbMap getInstance() {
        return MapperGsonToDbLoader.INSTANCE;
    }

    private static final class MapperGsonToDbLoader {
        private static final GsonToDbMap INSTANCE = new GsonToDbMap();
    }

    private GsonToDbMap() {
    }

    public CityWeatherWrapper convertGsonModelToDaoModel(CityGson cityGson) {
        return new CityWeatherWrapper(createDaoCity(cityGson, cityGson.getDateLastUpdate()), createDaoWeathers(cityGson.getWeathers()));
    }

    private CityDb createDaoCity(CityGson cityGson, String dateLastUpdateInfo) {
        return new CityDb(null, cityGson.getName(), dateLastUpdateInfo);
    }

    private List<WeatherDb> createDaoWeathers(List<WeatherRootGson> gsonWeather) {
        List<WeatherDb> weathers = new ArrayList<>();
        for (WeatherRootGson itemRoot : gsonWeather) {
            weathers.add(
                    new WeatherDb(
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
        return weathers;
    }
}
