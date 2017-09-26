package com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDb;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoSession;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.facade.FacadeMap;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.common.DbCommandUtils;

/**
 * Created by da.pavlov1 on 16.08.2017.
 */

public class AddCityInDbCommand extends DbCommandUtils implements DbCommand<Boolean> {
    private CityView city;

    public AddCityInDbCommand(CityView city) {
        this.city = city;
    }

    @Override
    public Boolean execute(DaoSession daoSession) {
        try {
            long cityId = daoSession.getCityDbDao().insert(
                    new CityDb(null, city.getName(), city.getLastTimeUpdate()));
            daoSession.getWeatherDbDao().insertInTx(
                    attachWeatherToCity(
                            FacadeMap.weatherVmToWeatherDb(city.getWeatherViews()), cityId, false));
        } catch (Exception e) {
            //Произошли ошибки при добавлении записи в Бд
            return false;
        }
        return true;
    }
}
