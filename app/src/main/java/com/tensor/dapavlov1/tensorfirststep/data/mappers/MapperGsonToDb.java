package com.tensor.dapavlov1.tensorfirststep.data.mappers;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DbCity;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DbWeather;
import com.tensor.dapavlov1.tensorfirststep.provider.common.TrimDateSingleton;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.ModelCityWeather;
import com.tensor.dapavlov1.tensorfirststep.data.gsonmodels.GsonCity;
import com.tensor.dapavlov1.tensorfirststep.data.gsonmodels.GsonWeatherRoot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by da.pavlov1 on 10.08.2017.
 */

public class MapperGsonToDb extends MapperHelp {
    public static MapperGsonToDb getInstance() {
        return MapperGsonToDbLoader.INSTANCE;
    }

    private static final class MapperGsonToDbLoader {
        private static final MapperGsonToDb INSTANCE = new MapperGsonToDb();
    }

    private MapperGsonToDb() {
    }

    public ModelCityWeather convertGsonModelToDaoModel(GsonCity gsonCity) {
        return new ModelCityWeather(createDaoCity(gsonCity, gsonCity.getDateLastUpdate()), createDaoWeathers(gsonCity.getWeathers()));
    }

    private DbCity createDaoCity(GsonCity gsonCity, String dateLastUpdateInfo) {
        return new DbCity(null, gsonCity.getName(), dateLastUpdateInfo);
    }

    private List<DbWeather> createDaoWeathers(List<GsonWeatherRoot> gsonWeather) {
        List<DbWeather> weathers = new ArrayList<>();
        for (GsonWeatherRoot itemRoot : gsonWeather) {
            weathers.add(
                    new DbWeather(
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
