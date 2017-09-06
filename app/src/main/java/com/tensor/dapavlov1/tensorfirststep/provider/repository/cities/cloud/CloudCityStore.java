//package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.cloud;
//
//import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDb;
//import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityWeatherWrapper;
//import com.tensor.dapavlov1.tensorfirststep.data.mappers.DbToViewMap;
//import com.tensor.dapavlov1.tensorfirststep.data.mappers.GsonToDbMap;
//import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
//import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.TempCity;
//import com.tensor.dapavlov1.tensorfirststep.provider.ApiFactory;
//import com.tensor.dapavlov1.tensorfirststep.provider.CreatorDbClient;
//import com.tensor.dapavlov1.tensorfirststep.provider.GsonFactory;
//import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;
//import com.tensor.dapavlov1.tensorfirststep.provider.client.WeatherApiClient;
//import com.tensor.dapavlov1.tensorfirststep.provider.common.TrimCityInfo;
//import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces.CityDataStore;
//import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyResponseException;
//
//import io.reactivex.Observable;
//
///**
// * Created by da.pavlov1 on 23.08.2017.
// */
//
//public class CloudCityStore {
//    private WeatherApiClient weatherClient = ApiFactory.getInstance().createClientWeatherApi();
//    private DbClient dbClient = CreatorDbClient.getInstance().createNewDaoClient();
//
//    public Observable<CityView> getCity(String fullCityName) throws EmptyResponseException {
//        return weatherClient
//                .getWeatherInCityRx(TrimCityInfo.getInstance().trimCityName(fullCityName))
//                .map(response -> {
//                    if (response == null || response.equals("")) {
//                        throw new EmptyResponseException();
//                    }
//                    return response;
//                })
//                .map(string -> GsonFactory.getInstance().createGsonCityModel(string))
//                .map(gsonCity -> GsonToDbMap.getInstance().convertGsonModelToDaoModel(gsonCity))
//                .map(mapper -> {
//                    cachedCity(mapper);
//                    return DbToViewMap.getInstance().convertDbModelToViewModel(mapper.getCityDb(), mapper.getWeathers(), false);
//                })
//                .map(viewCity -> {
//                    CityDb cityDb = dbClient.isAdd(viewCity.getName(), viewCity.getLastTimeUpdate());
//                    if (cityDb == null) {
//                        viewCity.setFavorite(false);
//                        return viewCity;
//                    }
//                    cachedCity(new CityWeatherWrapper(cityDb, cityDb.getWeathers()));
//                    viewCity.setFavorite(true);
//                    return viewCity;
//                });
//    }
//
//    private void cachedCity(CityWeatherWrapper tempCity) {
//        //запоминаем город в формате БД, для возможного добавления его в БД
//        TempCity.getInstance().setCityWeatherWrapper(tempCity);
//    }
//}
