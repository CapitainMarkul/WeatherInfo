package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.cloud;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityWeatherWrapper;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.DbToViewMap;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.GsonToDbMap;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.provider.ApiFactory;
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
    private WeatherApiClient weatherClient = ApiFactory.getInstance().createClientWeatherApi();

    private DbToViewMap dbToViewMap = DbToViewMap.getInstance();
    private GsonToDbMap gsonToDbMap = GsonToDbMap.getInstance();
    private GsonFactory gsonFactory = GsonFactory.getInstance();

    private List<String> cityNames = new ArrayList<>();

    public CloudCitiesDataStore(List<String> cityNames) {
        this.cityNames = cityNames;
    }

    @Override
    public Flowable<CityView> getCitiesRx() {
        return weatherClient.getWeatherInCityRx(cityNames)
                .map(string -> gsonFactory.createGsonCityModel(string))
                .map(gsonCity -> gsonToDbMap.convertGsonModelToDaoModel(gsonCity))
                .toFlowable(BackpressureStrategy.BUFFER)
                .switchMap(modelCityWeather -> {
                    //        //set Update weather info in DB
                    List<CityWeatherWrapper> list = new ArrayList<>();
                    list.add(modelCityWeather);
                    App.getExecutorService().execute(() -> dbClient.updateAllCities(list));
                    return Flowable.create((FlowableOnSubscribe<CityView>) e ->
                                    e.onNext(dbToViewMap.convertDbModelToViewModel(modelCityWeather.getCityDb(), modelCityWeather.getWeathers(), true)),
                            BackpressureStrategy.BUFFER);
                });
    }
}
