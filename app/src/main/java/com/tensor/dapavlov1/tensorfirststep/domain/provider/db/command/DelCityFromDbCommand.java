package com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDb;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDbDao;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoSession;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;

/**
 * Created by da.pavlov1 on 16.08.2017.
 */

public class DelCityFromDbCommand implements DbCommand<Boolean> {
    private final CityView city;

    public DelCityFromDbCommand(CityView city) {
        this.city = city;
    }

    @Override
    public Boolean execute(DaoSession daoSession) {
        try {
            CityDb temp = daoSession.getCityDbDao().queryBuilder()
                    .where(CityDbDao.Properties.Name.eq(city.getName()))
                    .unique();

            daoSession.getWeatherDbDao().deleteInTx(temp.getWeathers());
            temp.delete();
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
