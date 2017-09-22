package com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDb;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDbDao;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityWeatherWrapper;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoSession;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.common.TrimDateSingleton;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.common.DbCommandUtils;

import java.util.List;

/**
 * Created by da.pavlov1 on 21.09.2017.
 */

public class UpdateAllCitiesCommand extends DbCommandUtils implements DbCommand<Void> {
    private final List<CityWeatherWrapper> cityWeatherWrappers;

    public UpdateAllCitiesCommand(List<CityWeatherWrapper> cityWeatherWrappers) {
        this.cityWeatherWrappers = cityWeatherWrappers;
    }

    @Override
    public Void execute(DaoSession daoSession) {
        // FIXME: 12.09.2017 Подумать над использованием запроса
        for (CityWeatherWrapper newItem : cityWeatherWrappers) {
            CityDb cityDb = daoSession.getCityDbDao().queryBuilder()
                    .where(CityDbDao.Properties.Name.eq(newItem.getCityDb().getName()))
                    .unique();
            cityDb.setLastTimeUpdate(TrimDateSingleton.getInstance().getNowTime());
            cityDb.getWeathers().clear();
            cityDb.getWeathers().addAll(attachWeatherToCity(newItem.getWeathers(), cityDb.getId(), true));
            cityDb.update();
        }
        return null;
    }
}
