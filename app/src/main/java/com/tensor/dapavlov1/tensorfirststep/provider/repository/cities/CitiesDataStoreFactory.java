package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.provider.ApiFabric;
import com.tensor.dapavlov1.tensorfirststep.provider.CreatorDbClient;
import com.tensor.dapavlov1.tensorfirststep.provider.common.CheckConnect;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.cloud.CloudCitiesDataStore;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.cloud.CloudCityDataStore;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.database.DbCitiesDataStore;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.database.DbCityDataStore;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces.CitiesDataStore;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces.CityDataStore;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyDbException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.NetworkConnectException;

import java.util.List;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public class CitiesDataStoreFactory extends CheckConnect {
    public static CitiesDataStoreFactory getInstance() {
        return CitiesDataStoreFactoryLoader.INSTANCE;
    }

    private static final class CitiesDataStoreFactoryLoader {
        private static final CitiesDataStoreFactory INSTANCE = new CitiesDataStoreFactory();
    }

    private CitiesDataStoreFactory() {
    }

    //по сути, мы создаем тот же DbClient, только очень долгими путями
    public CityDataStore createCityDataStoreWorkDb() {
        return new DbCityDataStore();
    }

    //FavoriteActivity
    public CitiesDataStore createCitiesDataStore() throws EmptyDbException {
        //1. Начинаем читать БД
        //2.1. Если пусто, то возвращаем null
        //2.2. Если не пусто, начинаем проверять интернет, и возвращаем либо старое, либо обновленное


        //Здесь читаем БД, если пустая, то интернет нет смысла подключать
        List<String> cityNames =
                CreatorDbClient.getInstance().createNewDaoClient().getCityNames();

        //Решаем откуда будем брать информацию
        if (isOnline(App.getContext())) {
            return new CloudCitiesDataStore(cityNames);
        } else {
            return new DbCitiesDataStore();
        }
    }

    //AddCityActivity
    public CityDataStore createCityDataStore() throws NetworkConnectException {
        //здесь мы можем тянуть только из интернета
        if (isOnline(App.getContext())) {
            return new CloudCityDataStore();
        } else {
            throw new NetworkConnectException();
        }
    }
}




