package com.tensor.dapavlov1.tensorfirststep.domain.provider.db.common;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDb;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoSession;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.WeatherDb;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.exceptions.EmptyDbException;

import java.util.List;

/**
 * Created by da.pavlov1 on 21.09.2017.
 */

public abstract class DbCommandUtils {
    protected static List<WeatherDb> attachWeatherToCity(List<WeatherDb> weathers, Long cityId, boolean isUpdate) {
        long counter = 1;
        for (WeatherDb item : weathers) {
            item.setCityId(cityId);
            if (isUpdate) {
                item.setId(counter++);
            }
        }
        return weathers;
    }

    protected static List<CityDb> loadListAllCities(DaoSession daoSession) throws EmptyDbException {
        // TODO: 21.09.2017 ForCurrentThread. Лечить здесь
        List<CityDb> resultList =
                daoSession.getCityDbDao().queryBuilder().build().list();
        if (resultList.isEmpty()) {
            throw new EmptyDbException();
        }
        return resultList;
    }
}
