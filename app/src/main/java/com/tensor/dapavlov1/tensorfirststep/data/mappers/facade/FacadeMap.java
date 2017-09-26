package com.tensor.dapavlov1.tensorfirststep.data.mappers.facade;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDb;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityWeatherWrapper;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.WeatherDb;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.CityMapper;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.CityWeatherWrapMapper;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.WeatherMapper;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.WeatherView;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.GsonFactory;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities.mythrows.EmptyResponseException;

import java.util.List;


/**
 * Created by da.pavlov1 on 26.09.2017.
 */


public class FacadeMap {
    private FacadeMap() {

    }

    // TODO: 26.09.2017 Методы преобразования
    public static CityView jsonToVM(String json) throws EmptyResponseException {
        if (json == null || json.equals("")) {
            throw new EmptyResponseException();
        }

        CityWeatherWrapper wrapper =
                CityWeatherWrapMapper.convertGsonModelToWrapper(
                        GsonFactory.getInstance().createGsonCityModel(json));

        return CityMapper.convertDbModelToViewModel(wrapper.getCityDb(), wrapper.getWeathers(), true);
    }

    public static List<WeatherDb> weatherVmToWeatherDb(List<WeatherView> weathersView) {
        return WeatherMapper.convertVMWeatherToDbWeather(weathersView);
    }

    public static CityView cityDbToCityVM(CityDb city, boolean isFavorite) {
        return CityMapper.convertDbModelToViewModel(city, city.getWeathers(), isFavorite);
    }
}
