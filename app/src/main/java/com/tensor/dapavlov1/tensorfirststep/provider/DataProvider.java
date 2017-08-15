package com.tensor.dapavlov1.tensorfirststep.provider;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.Callback;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.TempCity;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.DaoCity;
import com.tensor.dapavlov1.tensorfirststep.provider.client.Dao;
import com.tensor.dapavlov1.tensorfirststep.GsonFactory;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.ModelCityWeather;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.MapperDbToView;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.MapperGsonToDb;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.provider.client.GoogleApi;
import com.tensor.dapavlov1.tensorfirststep.provider.client.WeatherApi;
import com.tensor.dapavlov1.tensorfirststep.provider.common.CheckConnect;
import com.tensor.dapavlov1.tensorfirststep.provider.common.TrimCityInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by da.pavlov1 on 14.08.2017.
 */

public class DataProvider {
    private static DataProvider instance;
    //todo: static ???
    private Dao daoClient;
    private WeatherApi weatherClient;
    private GoogleApi googleClient;

    private DataProvider() {
        initClients();
    }

    public static DataProvider getInstance() {
        if (instance == null) {
            instance = new DataProvider();
        }
        return instance;
    }

    private void initClients() {
        daoClient = WorkWithDataBase.getInstance().createNewDaoClient();
        weatherClient = ApiFabrica.getInstance().createClientWeatherApi();
    }

    public void updateCityInfo(final Callback callBack, final List<City> cities) throws IOException {
//        if (isOnline()) {
        //получаем обновленную информацию
        App.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                final List<ModelCityWeather> modelCityWeathers = new ArrayList<>();
                for (City item : cities) {
                    try {
                        modelCityWeathers.add(
                                MapperGsonToDb.getInstance().convertGsonModelToDaoModel(
                                        GsonFactory.getInstance().createGsonCityModel(
                                                weatherClient.getJsonFromApiWeather(item.getName())
                                        )
                                )
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //set Update in BD
                App.getExecutorService().execute(new Runnable() {
                    @Override
                    public void run() {
                        daoClient.updateAllCity(modelCityWeathers);
                    }
                });

                callBack.onSuccess(
                        MapperDbToView.getInstance().getCityViewModels(modelCityWeathers));
            }
        });
    }


    public void getCitiesFromBd(final Callback<List<City>> callBack) {
        App.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(
                        MapperDbToView.getInstance().getCityViewModelsFromDao(
                                daoClient.loadListAllCity()));
            }
        });
    }

    public void getWeathers(final Callback<City> callBack, final String cityName) {
        App.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String response = weatherClient.getJsonFromApiWeather(
                            TrimCityInfo.getInstance().trimCityName(cityName));

                    if (response != null && !response.equals("")) {
                        ModelCityWeather tempCity = MapperGsonToDb.getInstance().convertGsonModelToDaoModel(
                                GsonFactory.getInstance().createGsonCityModel(response));
                        //Получаем новый город и информацию о погоде в нем
//                        TempCity.getInstance().setModelCityWeather(
//                                MapperGsonToDb.getInstance().convertGsonModelToDaoModel(
//                                        GsonFactory.getInstance().createGsonCityModel(response)));

                        //проверяем, есть ли этот город в списке Favorite
                        DaoCity tempModel = daoClient.isAdd(tempCity.getDaoCity());

                        if (tempModel != null) {
                            tempCity = new ModelCityWeather(tempModel, tempModel.getWeathers());
                            callBack.onSuccess(
                                    MapperDbToView.getInstance().convertDbModelToViewModel(
                                            tempCity.getDaoCity(),
                                            tempCity.getWeathers()), true);
                        } else {
                            callBack.onSuccess(
                                    MapperDbToView.getInstance().convertDbModelToViewModel(
                                            tempCity.getDaoCity(),
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
                daoClient.deleteCity(position);
            }
        });
    }

    public void addCityToFavorite(final ModelCityWeather modelCityWeather) {
        App.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                daoClient.setInDataBase(
                        modelCityWeather.getDaoCity(),
                        modelCityWeather.getWeathers());
            }
        });
    }

    public void deleteCityFromDataBase(final ModelCityWeather modelCityWeather) {
        App.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                daoClient.deleteCity(modelCityWeather.getDaoCity());
            }
        });
//        daoClient.deleteCity(modelCityWeather.getDaoCity());
    }

    private boolean isOnline() {
        return CheckConnect.getInstance().isOnline(App.getContext());
    }
}
