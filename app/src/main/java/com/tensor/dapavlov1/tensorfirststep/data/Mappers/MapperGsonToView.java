package com.tensor.dapavlov1.tensorfirststep.data.Mappers;

import com.tensor.dapavlov1.tensorfirststep.TrimDateSingleton;
import com.tensor.dapavlov1.tensorfirststep.data.GsonModels.GsonCity;
import com.tensor.dapavlov1.tensorfirststep.data.GsonModels.GsonWeatherRoot;
import com.tensor.dapavlov1.tensorfirststep.data.ViewModels.City;
import com.tensor.dapavlov1.tensorfirststep.data.ViewModels.Weather;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by da.pavlov1 on 10.08.2017.
 */

public class MapperGsonToView extends MapperHelp {
    private static MapperGsonToView instance;

    private MapperGsonToView() {
    }

    public static MapperGsonToView getInstance() {
        if (instance == null) {
            instance = new MapperGsonToView();
        }
        return instance;
    }

    public City convertGsonModelToViewModel(GsonCity gsonCity) {
        return new City(gsonCity.getName(), TrimDateSingleton.getInstance().getNowTime(), getWeathersFromGson(gsonCity.getWeathers()));
    }

    private List<Weather> getWeathersFromGson(List<GsonWeatherRoot> gsonWeathers) {
        List<Weather> viewWeathers = new ArrayList<>();
        for (GsonWeatherRoot itemRoot : gsonWeathers) {
            viewWeathers.add(
                    new Weather(
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
        return viewWeathers;
    }
}
