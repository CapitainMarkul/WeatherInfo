package com.tensor.dapavlov1.tensorfirststep.data.mappers;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDb;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.WeatherDb;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.WeatherView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by da.pavlov1 on 26.09.2017.
 */

public class CityMapper {
    private CityMapper(){

    }

    //Работая с Оболочкой (Wrapper), нам требуется передать погоду Отдельно. (в городе Оболочки погоды нет)
    public static CityView convertDbModelToViewModel(CityDb cityDb, List<WeatherDb> weatherDb, boolean isFavorite) {
        return new CityView(cityDb.getName(), cityDb.getLastTimeUpdate(), getWeathers(weatherDb), isFavorite);
    }

    private static List<WeatherView> getWeathers(List<WeatherDb> weatherDb) {
        List<WeatherView> weatherViews = new ArrayList<>();
        for (WeatherDb itemBd : weatherDb) {
            weatherViews.add(new WeatherView(
                    itemBd.getWindShort(),
                    itemBd.getWindSpeed(),
                    itemBd.getPressure(),
                    itemBd.getTemperature(),
                    itemBd.getDate(),
                    itemBd.getTime(),
                    itemBd.getIconUrl(),
                    itemBd.getIconCode(),
                    itemBd.getDescription()
            ));
        }
        return weatherViews;
    }
}
