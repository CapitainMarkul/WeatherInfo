package com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.Wrapper.ResultWrapper;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.client.DbClient;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.common.CheckConnect;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities.cloudstore.CloudCitiesStore;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities.cloudstore.CloudCityStore;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities.dbstore.DbCitiesStore;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities.mythrows.EmptyDbException;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities.mythrows.NetworkConnectException;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public class CitiesDataRepository extends CheckConnect {
    private DbCitiesStore dbCitiesStore;
    private CloudCitiesStore cloudCitiesStore;
    private CloudCityStore cloudCityStore;

    public CitiesDataRepository() {
        dbCitiesStore = new DbCitiesStore();
        cloudCitiesStore = new CloudCitiesStore();
        cloudCityStore = new CloudCityStore();
    }

    public ResultWrapper<Observable<CityView>> getCity(String fullCityName) {
        //  здесь мы можем тянуть только из интернета
        if (!isOnline(App.get())) {
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

        if (isOnline(App.get())) {
            return new ResultWrapper<>(cloudCitiesStore.getCitiesRx(cityNames), null);
        } else {
            return new ResultWrapper<>(dbCitiesStore.getCitiesRx(), new NetworkConnectException());
        }
    }
}
