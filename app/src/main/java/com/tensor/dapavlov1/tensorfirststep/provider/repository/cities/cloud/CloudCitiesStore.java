package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.cloud;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDb;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityWeatherWrapper;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.DbToViewMap;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.GsonToDbMap;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.TempCity;
import com.tensor.dapavlov1.tensorfirststep.provider.ApiFactory;
import com.tensor.dapavlov1.tensorfirststep.provider.CreatorDbClient;
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
    private DbClient dbClient = CreatorDbClient.getInstance().createNewDaoClient();
    private WeatherApiClient weatherClient = ApiFactory.getInstance().createClientWeatherApi();

    private DbToViewMap dbToViewMap = DbToViewMap.getInstance();
    private GsonToDbMap gsonToDbMap = GsonToDbMap.getInstance();
    private GsonFactory gsonFactory = GsonFactory.getInstance();

//    private List<String> cityNames = new ArrayList<>();

    public Flowable<CityView> getCitiesRx(List<String> cityNames) {
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
                .map(gsonCity -> GsonToDbMap.getInstance().convertGsonModelToDaoModel(gsonCity))
                .map(mapper -> {
                    cachedCity(mapper);
                    return DbToViewMap.getInstance().convertDbModelToViewModel(mapper.getCityDb(), mapper.getWeathers(), false);
                })
                .map(viewCity -> {
//                    CityDb cityDb = dbClient.isAdd(viewCity.getName(), viewCity.getLastTimeUpdate());
                    CityDb cityDb = dbClient.isAdd(viewCity.getName(), viewCity.getLastTimeUpdate());
                    if (cityDb == null) {
                        viewCity.setFavorite(false);
                        return viewCity;
                    }
                    cachedCity(new CityWeatherWrapper(cityDb, cityDb.getWeathers()));
                    viewCity.setFavorite(true);
                    return viewCity;
                });
    }

    private void cachedCity(CityWeatherWrapper tempCity) {
        //запоминаем город в формате БД, для возможного добавления его в БД
        TempCity.getInstance().setCityWeatherWrapper(tempCity);
    }

    private String trimCityName(String fullCityName) {
        if (fullCityName.indexOf(',') != -1) {
            return fullCityName.substring(0, fullCityName.indexOf(','));
        } else {
            return fullCityName;
        }
    }
}
