package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.ViewToDbMap;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.provider.callbacks.CityCallback;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;
import com.tensor.dapavlov1.tensorfirststep.provider.common.CheckConnect;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.cloud.CloudCitiesStore;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.database.DbCitiesStore;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces.CitiesRepository;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyDbException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyResponseException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.NetworkConnectException;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public class CitiesDataRepository extends CheckConnect implements CitiesRepository {
    private DbCitiesStore dbCitiesStore = new DbCitiesStore();
    private CloudCitiesStore cloudCitiesStore = new CloudCitiesStore();

    @Override
    public void add(CityView city) {
        dbCitiesStore.add(city.getName(), city.getLastTimeUpdate(),
                ViewToDbMap.convertWeathersToDbType(city.getWeatherViews()));
    }

    @Override
    public void delete(String cityName) {
        dbCitiesStore.delete(cityName);
    }


    @Override
    public void getCity(String fullCityName, CityCallback<CityView> callbackCities) {
        //здесь мы можем тянуть только из интернета
        if (!isOnline(App.getContext())) {
            callbackCities.onErrorConnect();
            return;
        }

        try {
            cloudCitiesStore.getCity(fullCityName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            result -> {
                                if (result.isFavorite()) {
                                    callbackCities.isFavoriteCity(result);
                                } else {
                                    callbackCities.isNotFavoriteCity(result);
                                }
                            },
                            error -> {
                                if (error instanceof EmptyDbException) {
                                    callbackCities.isEmpty();
                                } else if (error instanceof NetworkConnectException) {
                                    callbackCities.onErrorConnect();
                                } else {
                                    // если случилось что-то непредусмотренное, показываем карточку "Город не найден"
                                    callbackCities.isEmpty();
                                }
                            },
                            () -> {
                            });
//                            },
//                            disposable -> DisposableManager.addDisposable(disposable));
        } catch (EmptyResponseException e) {
            callbackCities.isEmpty();
        }
    }


    @Override
    public Flowable<CityView> getCitiesRx() throws EmptyDbException {
        //1. Начинаем читать БД
        //2.1. Если пусто, то возвращаем null
        //2.2. Если не пусто, начинаем проверять интернет, и возвращаем либо старое, либо обновленное

        //Здесь читаем БД, если пустая, то интернет нет смысла подключать
        List<String> cityNames =
                DbClient.getInstance().getCityNames();

//        Решаем откуда будем брать информацию
        if (isOnline(App.getContext())) {
            return cloudCitiesStore.getCitiesRx(cityNames);
        } else {
            // TODO: 06.09.2017 А как отсюда дать пользователю понять, что сеть отсутствует? Здесь нет CallBack который можно вызвать
            return dbCitiesStore.getCitiesRx();
        }
    }
}
