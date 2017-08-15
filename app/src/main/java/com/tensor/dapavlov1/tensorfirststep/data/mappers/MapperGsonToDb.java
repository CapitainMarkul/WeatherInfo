package com.tensor.dapavlov1.tensorfirststep.data.mappers;

import com.tensor.dapavlov1.tensorfirststep.provider.common.TrimDateSingleton;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoCity;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoWeather;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.ModelCityWeather;
import com.tensor.dapavlov1.tensorfirststep.data.gsonmodels.GsonCity;
import com.tensor.dapavlov1.tensorfirststep.data.gsonmodels.GsonWeatherRoot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by da.pavlov1 on 10.08.2017.
 */

public class MapperGsonToDb extends MapperHelp {
    private static MapperGsonToDb instance;

    private MapperGsonToDb() {
    }

    public static MapperGsonToDb getInstance() {
        if (instance == null) {
            instance = new MapperGsonToDb();
        }
        return instance;
    }

    public ModelCityWeather convertGsonModelToDaoModel(GsonCity gsonCity) {
        return new ModelCityWeather(createDaoCity(gsonCity, gsonCity.getDateLastUpdate()), createDaoWeathers(gsonCity.getWeathers()));
    }

    private DaoCity createDaoCity(GsonCity gsonCity, String dateLastUpdateInfo) {
        return new DaoCity(null, gsonCity.getName(), dateLastUpdateInfo);
    }

    private List<DaoWeather> createDaoWeathers(List<GsonWeatherRoot> gsonWeather) {
        List<DaoWeather> weathers = new ArrayList<>();
        for (GsonWeatherRoot itemRoot : gsonWeather) {
            weathers.add(
                    new DaoWeather(
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
