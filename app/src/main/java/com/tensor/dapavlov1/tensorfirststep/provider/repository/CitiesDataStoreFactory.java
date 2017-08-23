package com.tensor.dapavlov1.tensorfirststep.provider.repository;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.provider.common.CheckConnect;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public class CitiesDataStoreFactory {
    private static final CitiesDataStoreFactory ourInstance = new CitiesDataStoreFactory();

    public static CitiesDataStoreFactory getInstance() {
        return ourInstance;
    }

    private CitiesDataStoreFactory() {
    }

    public CitiesDataStore create() {
        //1. Начинаем читать БД
        //2.1. Если пусто, то возвращаем null
        //2.2. Если не пусто, начинаем проверять интернет, и возвращаем либо старое, либо обновленное

        //Решаем откуда будем брать информацию
        if (isOnline()) {
            return new CloudCitiesDataStore();
        } else {
            return new DbCitiesDataStore();
        }
    }

    private boolean isOnline() {
        return CheckConnect.getInstance().isOnline(App.getContext());
    }
}




