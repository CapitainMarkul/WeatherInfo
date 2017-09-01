package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.cloud;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DbCity;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.ModelCityWeather;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.MapperDbToView;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.MapperGsonToDb;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.TempCity;
import com.tensor.dapavlov1.tensorfirststep.provider.ApiFabric;
import com.tensor.dapavlov1.tensorfirststep.provider.CreatorDbClient;
import com.tensor.dapavlov1.tensorfirststep.provider.GsonFactory;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;
import com.tensor.dapavlov1.tensorfirststep.provider.client.WeatherApiClient;
import com.tensor.dapavlov1.tensorfirststep.provider.common.TrimCityInfo;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces.CityDataStore;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyResponseException;

import io.reactivex.Observable;

/**
 * Created by da.pavlov1 on 23.08.2017.
 */

public class CloudCityDataStore implements CityDataStore {
    private WeatherApiClient weatherClient = ApiFabric.getInstance().createClientWeatherApi();
    private DbClient dbClient = CreatorDbClient.getInstance().createNewDaoClient();

    // TODO: 24.08.2017 Пустые методы
    @Override
    public void add(ModelCityWeather city) {

    }

    @Override
    public void delete(Object city) {

    }

    @Override
    public Observable<City> getCity(String fullCityName) throws EmptyResponseException {
        return weatherClient
                .observableWeatherResponseRx(TrimCityInfo.getInstance().trimCityName(fullCityName))
                .map(response -> {
                    if (response == null || response.equals("")) {
                        throw new EmptyResponseException();
                    }
                    return response;
                })
                .map(string -> GsonFactory.getInstance().createGsonCityModel(string))
                .map(gsonCity -> MapperGsonToDb.getInstance().convertGsonModelToDaoModel(gsonCity))
                .map(mapper -> {
                    cachedCity(mapper);
                    return MapperDbToView.getInstance().convertDbModelToViewModel(mapper.getDbCity(), mapper.getWeathers(), false);
                })
                .map(viewCity -> {
                    DbCity dbCity = dbClient.isAdd(viewCity.getName(), viewCity.getLastTimeUpdate());
                    if (dbCity == null) {
                        viewCity.setFavorite(false);
                        return viewCity;
                    }
                    cachedCity(new ModelCityWeather(dbCity, dbCity.getWeathers()));   // todo Вспомнить
                    viewCity.setFavorite(true);
                    return viewCity;
                });
    }

    private void cachedCity(ModelCityWeather tempCity) {
        //запоминаем город в формате БД, для возможного добавления его в БД
        TempCity.getInstance().setModelCityWeather(tempCity);
    }
}
