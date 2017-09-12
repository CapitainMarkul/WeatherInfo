package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.cloud;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDb;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityWeatherWrapper;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.DbToViewMap;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.GsonToDbMap;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.GsonToViewMap;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.provider.ApiFactory;
import com.tensor.dapavlov1.tensorfirststep.provider.GsonFactory;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;
import com.tensor.dapavlov1.tensorfirststep.provider.client.WeatherApiClient;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyResponseException;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;

/**
 * Created by da.pavlov1 on 23.08.2017.
 */

public class CloudCitiesStore {
    private DbClient dbClient = DbClient.getInstance();
    private WeatherApiClient weatherClient = ApiFactory.getInstance().createClientWeatherApi();

    private DbToViewMap dbToViewMap = DbToViewMap.getInstance();
    private GsonToDbMap gsonToDbMap = GsonToDbMap.getInstance();
    private GsonFactory gsonFactory = GsonFactory.getInstance();


    // FIXME: 12.09.2017 Методы возвращают одинаковое, подумать как склеить
    public Flowable<CityView> getCitiesRx(List<String> cityNames) {
        return weatherClient.getWeatherInCityRx(cityNames)
                .map(string -> gsonFactory.createGsonCityModel(string))
                .map(gsonCity -> gsonToDbMap.convertGsonModelToDaoModel(gsonCity))
                .toFlowable(BackpressureStrategy.BUFFER)
                .switchMap(cityWeatherWrapper -> {
                    //set Update weather info in DB
                    App.getExecutorService().execute(() -> dbClient.updateCity(cityWeatherWrapper));
                    return Flowable.just(dbToViewMap.convertDbModelToViewModel(cityWeatherWrapper.getCityDb(), cityWeatherWrapper.getWeathers(), true));
                });
    }

    public Observable<CityView> getCity(String fullCityName) throws EmptyResponseException {
        return weatherClient
                .getWeatherInCityRx(trimCityName(fullCityName))
                .map(response -> {
                    if (response == null || response.equals("")) {
                        throw new EmptyResponseException();
                    }
                    return response;
                })
                .map(string -> GsonFactory.getInstance().createGsonCityModel(string))
                .map(cityGson -> GsonToViewMap.getInstance().convertGsonToViewModel(cityGson))
                .map(viewCity -> {
                    CityDb cityDb = dbClient.searchCity(viewCity.getName());
                    if (cityDb == null) {
                        viewCity.setFavorite(false);
                        return viewCity;
                    }
                    viewCity.setFavorite(true);
                    return viewCity;
                });
    }

    private String trimCityName(String fullCityName) {
        if (fullCityName.indexOf(',') != -1) {
            return fullCityName.substring(0, fullCityName.indexOf(','));
        } else {
            return fullCityName;
        }
    }
}
