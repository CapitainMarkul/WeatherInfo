package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities;

import com.tensor.dapavlov1.tensorfirststep.DisposableManager;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.ModelCityWeather;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.provider.callbacks.CallbackCity;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces.CitiesDataStore;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces.CitiesRepository;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces.CityDataStore;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyDbException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyResponseException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.NetworkConnectException;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public class CitiesDataRepository implements CitiesRepository {
    private CitiesDataStoreFactory citiesDataStoreFactory = CitiesDataStoreFactory.getInstance();

    @Override
    public void add(ModelCityWeather city) {
        citiesDataStoreFactory.createCityDataStoreWorkDb().add(city);
    }

    @Override
    public void delete(Object city) {
        citiesDataStoreFactory.createCityDataStoreWorkDb().delete(city);
    }

    @Override
    public void getCity(String fullCityName, CallbackCity<City> callbackCities) {
        try {
            CityDataStore cityDataStore = citiesDataStoreFactory.createCityDataStore();
            cityDataStore.getCity(fullCityName)
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
                            },
                            disposable -> DisposableManager.addDisposable(disposable));
        } catch (NetworkConnectException e) {
            callbackCities.onErrorConnect();
        } catch (EmptyResponseException e) {
            callbackCities.isEmpty();
        }
    }

    @Override
    public Flowable<City> getCitiesRx() {
        //определяем откуда тянуть информацию/ Из бд или из сети
        CitiesDataStore citiesDataStore;
        try {
            citiesDataStore = citiesDataStoreFactory.createCitiesDataStore();
        } catch (EmptyDbException e) {
            return Flowable.empty();
        }
        return citiesDataStore.getCitiesRx();
    }
}
