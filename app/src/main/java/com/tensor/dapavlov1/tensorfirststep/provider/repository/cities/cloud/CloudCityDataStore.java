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
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.CityFoundException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.CityNotFoundException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyDbException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyResponseException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.NetworkConnectException;

import java.io.IOException;

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
    public City getCity(String fullCityName) throws NetworkConnectException, EmptyResponseException, CityNotFoundException, CityFoundException {
        String response;
        try {
            response = weatherClient.getJsonFromApiWeather(
                    TrimCityInfo.getInstance().trimCityName(fullCityName));
        } catch (IOException e) {
            //если проблемы с получением ответа
            throw new NetworkConnectException();
        }

        if (response != null && !response.equals("")) {
            ModelCityWeather tempCity = MapperGsonToDb.getInstance().convertGsonModelToDaoModel(
                    GsonFactory.getInstance().createGsonCityModel(response));

            try {
                //проверяем, есть ли этот город в списке Favorite
                DbCity dbCity = dbClient.isAdd(tempCity.getDbCity());

                //Если город не найден в бд
                if (dbCity == null) {
                    cachedCity(tempCity);
                    throw new CityNotFoundException(
                            MapperDbToView.getInstance().convertDbModelToViewModel(
                                    tempCity.getDbCity(),
                                    tempCity.getWeathers()));
                }

                cachedCity(new ModelCityWeather(dbCity, dbCity.getWeathers()));
                throw new CityFoundException(
                        MapperDbToView.getInstance().convertDbModelToViewModel(
                                tempCity.getDbCity(),
                                tempCity.getWeathers()));
            } catch (EmptyDbException e) {
                //если БД пуста
                cachedCity(tempCity);
                throw new CityNotFoundException(
                        MapperDbToView.getInstance().convertDbModelToViewModel(
                                tempCity.getDbCity(),
                                tempCity.getWeathers()));
            }
        } else {
            throw new EmptyResponseException();
        }
    }

    private void cachedCity(ModelCityWeather tempCity) {
        //запоминаем город в формате БД, для возможного добавления его в БД
        TempCity.getInstance().setModelCityWeather(tempCity);
    }
}
