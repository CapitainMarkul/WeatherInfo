package com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDb;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDbDao;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoSession;

/**
 * Created by da.pavlov1 on 21.09.2017.
 */

public class SearchCityCommand implements DbCommand<CityDb> {
    private final String cityName;

    public SearchCityCommand(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public CityDb execute(DaoSession daoSession) {
        return daoSession.getCityDbDao().queryBuilder()
                .where(CityDbDao.Properties.Name.eq(cityName))
                .unique();
    }
}
