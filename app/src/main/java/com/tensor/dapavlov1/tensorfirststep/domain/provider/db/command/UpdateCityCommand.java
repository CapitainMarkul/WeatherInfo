package com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDb;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDbDao;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityWeatherWrapper;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoSession;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.common.TrimDateSingleton;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.common.DbCommandHelper;

/**
 * Created by da.pavlov1 on 21.09.2017.
 */

public class UpdateCityCommand extends DbCommandHelper implements DbCommand<Void> {
    private final CityWeatherWrapper cityWeatherWrapper;

    public UpdateCityCommand(CityWeatherWrapper cityWeatherWrapper) {
        this.cityWeatherWrapper = cityWeatherWrapper;
    }

    @Override
    public Void execute(DaoSession daoSession) {
        CityDb cityDb = daoSession.getCityDbDao().queryBuilder()
                .where(CityDbDao.Properties.Name.eq(cityWeatherWrapper.getCityDb().getName()))
                .unique();
        cityDb.setLastTimeUpdate(TrimDateSingleton.getInstance().getNowTime());
        cityDb.getWeathers().clear();
        cityDb.getWeathers().addAll(attachWeatherToCity(cityWeatherWrapper.getWeathers(), cityDb.getId(), true));
        cityDb.update();
        return null;
    }
}
