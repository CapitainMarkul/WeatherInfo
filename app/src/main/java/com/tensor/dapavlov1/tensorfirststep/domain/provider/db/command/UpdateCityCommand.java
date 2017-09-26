package com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDb;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDbDao;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoSession;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.facade.FacadeMap;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.WeatherView;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.common.TrimDateSingleton;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.common.DbCommandUtils;

import java.util.List;

/**
 * Created by da.pavlov1 on 21.09.2017.
 */

public class UpdateCityCommand extends DbCommandUtils implements DbCommand<Void> {
    //    private final CityWeatherWrapper cityWeatherWrapper;
    private final String cityname;
    private final List<WeatherView> weathers;

//    public UpdateCityCommand(CityWeatherWrapper cityWeatherWrapper) {
//        this.cityWeatherWrapper = cityWeatherWrapper;
//    }

    public UpdateCityCommand(String cityname, List<WeatherView> weathers) {
        this.cityname = cityname;
        this.weathers = weathers;
    }

    @Override
    public Void execute(DaoSession daoSession) {
        CityDb cityDb = daoSession.getCityDbDao().queryBuilder()
                .where(CityDbDao.Properties.Name.eq(cityname))
                .unique();
        cityDb.setLastTimeUpdate(TrimDateSingleton.getInstance().getNowTime());
        cityDb.getWeathers().clear();
        cityDb.getWeathers().addAll(
                attachWeatherToCity(
                        FacadeMap.weatherVmToWeatherDb(weathers), cityDb.getId(), true));
        cityDb.update();
        return null;
    }
}
