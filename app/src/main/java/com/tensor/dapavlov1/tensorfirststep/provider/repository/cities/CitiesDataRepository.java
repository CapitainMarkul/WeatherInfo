package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.ViewToDbMap;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.Wrapper.ResultWrapper;
import com.tensor.dapavlov1.tensorfirststep.provider.callbacks.CityCallback;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;
import com.tensor.dapavlov1.tensorfirststep.provider.common.CheckConnect;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.cloudstore.CloudCitiesStore;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.cloudstore.CloudCityStore;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.dbstore.DbCitiesStore;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces.CitiesRepository;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyDbException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyResponseException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.NetworkConnectException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.deleteobservable.DelObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public class CitiesDataRepository extends CheckConnect implements CitiesRepository, DelObserver {
    private DbCitiesStore dbCitiesStore;
    private CloudCitiesStore cloudCitiesStore;
    private CloudCityStore cloudCityStore;
    private List<CityView> cachedCitiesView;

    public CitiesDataRepository() {
        dbCitiesStore = new DbCitiesStore();
        cloudCitiesStore = new CloudCitiesStore();
        cloudCityStore = new CloudCityStore();
        cachedCitiesView = new ArrayList<>();
    }

    public List<CityView> getCacheCitiesView() {
        return cachedCitiesView;
    }

    public void cacheCityView(CityView cityView) {
        cachedCitiesView.add(cityView);
    }

    public void clearCacheCitiesView() {
        cachedCitiesView.clear();
    }

    @Override
    public void deleteResult(boolean isSuccess, String deleteCityName) {
        if (isSuccess) {
            List<CityView> cachedCities = getCacheCitiesView();
            for (CityView item : cachedCities) {
                if (item.getName().equals(deleteCityName)) {
                    cachedCities.remove(item);
                    break;
                }
            }
        }
        DbClient.getInstance().unSubscribe(this);
    }

    @Override
    public void add(CityView city) {
        dbCitiesStore.add(city.getName(), city.getLastTimeUpdate(),
                ViewToDbMap.convertWeathersToDbType(city.getWeatherViews()));
    }

    @Override
    public void delete(String cityName) {
        DbClient.getInstance().subscribe(this);
        dbCitiesStore.delete(cityName);
    }

    public ResultWrapper<Observable<CityView>> getCity(String fullCityName) {
        //  здесь мы можем тянуть только из интернета
        if (!isOnline(App.getContext())) {
            return new ResultWrapper<>(null, new NetworkConnectException());
        }
        return new ResultWrapper<>(cloudCityStore.getCity(fullCityName), null);
    }

    public ResultWrapper<Flowable<CityView>> getCitiesRx() {
        //1. Начинаем читать БД
        //2.1. Если пусто, то возвращаем null
        //2.2. Если не пусто, начинаем проверять интернет, и возвращаем либо старое, либо обновленное

        //Здесь читаем БД, если пустая, то интернет нет смысла подключать
        List<String> cityNames;
        try {
            cityNames = DbClient.getInstance().getCityNames();
        } catch (EmptyDbException e) {
            return new ResultWrapper<>(null, e);
        }

        if (isOnline(App.getContext())) {
            return new ResultWrapper<>(cloudCitiesStore.getCitiesRx(cityNames), null);
        } else {
            return new ResultWrapper<>(dbCitiesStore.getCitiesRx(), new NetworkConnectException());
        }
    }
}
