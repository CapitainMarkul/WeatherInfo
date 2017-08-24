package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.cloud;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.ModelCityWeather;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.MapperDbToView;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.MapperGsonToDb;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.provider.ApiFabric;
import com.tensor.dapavlov1.tensorfirststep.provider.CreatorDbClient;
import com.tensor.dapavlov1.tensorfirststep.provider.GsonFactory;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;
import com.tensor.dapavlov1.tensorfirststep.provider.client.WeatherApiClient;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces.CitiesDataStore;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyDbException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.NetworkConnectException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by da.pavlov1 on 23.08.2017.
 */

public class CloudCitiesDataStore implements CitiesDataStore {
    private DbClient dbClient = CreatorDbClient.getInstance().createNewDaoClient();
    private WeatherApiClient weatherClient = ApiFabric.getInstance().createClientWeatherApi();

    @Override
    public List<City> getCities() throws NetworkConnectException, EmptyDbException {
        try {
            return updateCityInfo(
                    MapperDbToView.getInstance().getCityViewModelsFromDao(dbClient.loadListAllCity()));
        } catch (IOException e) {
            //вернем пустую карточку
            throw new EmptyDbException();
        }
    }

    private List<City> updateCityInfo(final List<City> cities) throws IOException {
        //получаем обновленную информацию
        final List<ModelCityWeather> modelCityWeathers = new ArrayList<>();
        for (City item : cities) {
            modelCityWeathers.add(
                    MapperGsonToDb.getInstance().convertGsonModelToDaoModel(
                            GsonFactory.getInstance().createGsonCityModel(
                                    weatherClient.getJsonFromApiWeather(item.getName())
                            )
                    )
            );
        }
        //set Update in BD
        App.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                dbClient.updateAllCity(modelCityWeathers);
            }
        });

        return MapperDbToView.getInstance().getCityViewModels(modelCityWeathers);
    }
}
