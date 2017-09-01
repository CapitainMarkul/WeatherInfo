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

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by da.pavlov1 on 23.08.2017.
 */

public class CloudCitiesDataStore implements CitiesDataStore {
    private DbClient dbClient = CreatorDbClient.getInstance().createNewDaoClient();
    private WeatherApiClient weatherClient = ApiFabric.getInstance().createClientWeatherApi();

    private MapperDbToView dbToViewMap = MapperDbToView.getInstance();
    private MapperGsonToDb gsonToDbMap = MapperGsonToDb.getInstance();
    private GsonFactory gsonFactory = GsonFactory.getInstance();

    private List<String> cityNames = new ArrayList<>();

    public CloudCitiesDataStore(List<String> cityNames) {
        this.cityNames = cityNames;
    }

    @Override
    public Flowable<City> getCitiesRx() {
        return weatherClient.observableWeathersResponseRx(cityNames)
                .map(string -> gsonFactory.createGsonCityModel(string))
                .map(gsonCity -> gsonToDbMap.convertGsonModelToDaoModel(gsonCity))
                .toFlowable(BackpressureStrategy.BUFFER)
                .switchMap(modelCityWeather -> {
                    // TODO: 31.08.2017 Можно переписать метод для сохранения одного элемента
                    //        //set Update weather info in DB
                    List<ModelCityWeather> list = new ArrayList<>();
                    list.add(modelCityWeather);
                    App.getExecutorService().execute(() -> dbClient.updateAllCities(list));
                    return Flowable.create((FlowableOnSubscribe<City>) e ->
                                    e.onNext(dbToViewMap.convertDbModelToViewModel(modelCityWeather.getDbCity(), modelCityWeather.getWeathers(), true)),
                            BackpressureStrategy.BUFFER);
                });
    }
}
