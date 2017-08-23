package com.tensor.dapavlov1.tensorfirststep.provider.repository;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.provider.Callback;
import com.tensor.dapavlov1.tensorfirststep.provider.CallbackCities;
import com.tensor.dapavlov1.tensorfirststep.provider.DataProvider;
import com.tensor.dapavlov1.tensorfirststep.provider.common.CheckConnect;

import java.io.IOException;
import java.util.List;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public class CitiesDataStoreFactory {
    private static final CitiesDataStoreFactory ourInstance = new CitiesDataStoreFactory();

    public static CitiesDataStoreFactory getInstance() {
        return ourInstance;
    }

    private CitiesDataStoreFactory() {
        dataProvider = DataProvider.getInstance();
    }

    private DataProvider dataProvider;

    public void getCities(CallbackCities<List<City>> callbackResult) throws IOException {
        // TODO: 22.08.2017 Здесь обращение к внешнему Api + Запуск дествия в пуле потоков + не забыть про кеширование в Презентере
        //1. Начинаем читать БД
        //2.1. Если пусто, то возвращаем null
        //2.2. Если не пусто, начинаем проверять интернет, и возвращаем либо старое, либо обновленное
        List<City> citiesFromDb = dataProvider.getCitiesFromBd();

        if (citiesFromDb.isEmpty()) {
            callbackResult.isEmpty();
        } else {
            if (isOnline()) {
                callbackResult.onUpdate(
                        dataProvider.updateCityInfo(citiesFromDb));
            } else {
                callbackResult.onOldFromDb(citiesFromDb);
            }
        }
    }

    private boolean isOnline() {
        return CheckConnect.getInstance().isOnline(App.getContext());
    }
}




