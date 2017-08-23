package com.tensor.dapavlov1.tensorfirststep.provider;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DbCity;
import com.tensor.dapavlov1.tensorfirststep.provider.client.WeatherApiClient;
import com.tensor.dapavlov1.tensorfirststep.provider.commands.AddCityInDbCommand;
import com.tensor.dapavlov1.tensorfirststep.provider.commands.DelCityByIndexCommand;
import com.tensor.dapavlov1.tensorfirststep.provider.invokers.RemoteControlDb;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.TempCity;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.ModelCityWeather;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.MapperDbToView;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.MapperGsonToDb;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.provider.client.GoogleApiClient;
import com.tensor.dapavlov1.tensorfirststep.provider.common.CheckConnect;
import com.tensor.dapavlov1.tensorfirststep.provider.common.TrimCityInfo;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.mythrows.EmptyDbException;

import java.io.IOException;
import java.util.List;

/**
 * Created by da.pavlov1 on 14.08.2017.
 */

public class DataProvider {
    private static DataProvider instance;

    private DbClient dbClient;
    private WeatherApiClient weatherClient;
//    weatherClient = ApiFabric.getInstance().createClientWeatherApi();
    private GoogleApiClient googleClient;
    private RemoteControlDb remoteControlDb;

    private DataProvider() {
        init();
    }

    public static DataProvider getInstance() {
        if (instance == null) {
            instance = new DataProvider();
        }
        return instance;
    }

    private void init() {
        dbClient = CreatorDbClient.getInstance().createNewDaoClient();
        weatherClient = ApiFabric.getInstance().createClientWeatherApi();
        googleClient = ApiFabric.getInstance().crateClientGoogleApi();

        remoteControlDb = new RemoteControlDb();
    }

    public List<String> getPlaces(String inputText) throws IOException {
        return GsonFactory.getInstance().getPlacesName(
                googleClient.getJsonFromGooglePlaceApi(inputText));
    }

//    public List<City> updateCityInfo(final List<City> getCities) throws IOException {
//        //получаем обновленную информацию
//        final List<ModelCityWeather> modelCityWeathers = new ArrayList<>();
//        for (City item : getCities) {
//            try {
//                modelCityWeathers.add(
//                        MapperGsonToDb.getInstance().convertGsonModelToDaoModel(
//                                GsonFactory.getInstance().createGsonCityModel(
//                                        weatherClient.getJsonFromApiWeather(item.getName())
//                                )
//                        )
//                );
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        //set Update in BD
//        App.getExecutorService().execute(new Runnable() {
//            @Override
//            public void run() {
//                dbClient.updateAllCity(modelCityWeathers);
//            }
//        });
//
//        return MapperDbToView.getInstance().getCityViewModels(modelCityWeathers);
//    }

//    public List<City> getCitiesFromBd() {
//        return MapperDbToView.getInstance().getCityViewModelsFromDao(dbClient.loadListAllCity());
//    }

    public void getWeathers(final String cityName, final Callback<City> callBack) {
        App.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String response = weatherClient.getJsonFromApiWeather(
                            TrimCityInfo.getInstance().trimCityName(cityName));

                    if (response != null && !response.equals("")) {
                        ModelCityWeather tempCity = MapperGsonToDb.getInstance().convertGsonModelToDaoModel(
                                GsonFactory.getInstance().createGsonCityModel(response));

                        //проверяем, есть ли этот город в списке Favorite
                        DbCity tempModel = null;
                        try {
                            tempModel = dbClient.isAdd(tempCity.getDbCity());
                        } catch (EmptyDbException e) {
                            e.printStackTrace();
                        }

                        if (tempModel != null) {
                            tempCity = new ModelCityWeather(tempModel, tempModel.getWeathers());
                            callBack.onSuccess(
                                    MapperDbToView.getInstance().convertDbModelToViewModel(
                                            tempCity.getDbCity(),
                                            tempCity.getWeathers()), true);
                        } else {
                            callBack.onSuccess(
                                    MapperDbToView.getInstance().convertDbModelToViewModel(
                                            tempCity.getDbCity(),
                                            tempCity.getWeathers()), false);
                        }

                        TempCity.getInstance().setModelCityWeather(tempCity);
                    } else {
                        callBack.onFail();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onFail(null);
                }
            }
        });
    }

    public void deleteCity(final int position) {
        App.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                remoteControlDb.setCommand(
                        new DelCityByIndexCommand(dbClient, position));
                remoteControlDb.execute();
            }
        });
    }

    public void addCityToFavorite(final ModelCityWeather modelCityWeather) {
        App.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                remoteControlDb.setCommand(
                        new AddCityInDbCommand(dbClient, modelCityWeather));
                remoteControlDb.execute();
            }
        });
    }

    public void deleteCity(final ModelCityWeather modelCityWeather) {
        App.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                remoteControlDb.setCommand(
                        new AddCityInDbCommand(dbClient, modelCityWeather));
                remoteControlDb.undo();
            }
        });
    }

    private boolean isOnline() {
        return CheckConnect.getInstance().isOnline(App.getContext());
    }
}
