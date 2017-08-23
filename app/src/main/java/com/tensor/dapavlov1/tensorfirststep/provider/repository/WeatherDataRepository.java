package com.tensor.dapavlov1.tensorfirststep.provider.repository;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.provider.CallbackCities;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.mythrows.EmptyDbException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.mythrows.NetworkConnectionException;

import java.util.List;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public class WeatherDataRepository implements WeatherRepository {
    private CitiesDataStoreFactory citiesDataStoreFactory;

    public WeatherDataRepository() {
        citiesDataStoreFactory = CitiesDataStoreFactory.getInstance();
    }

    @Override
    public City city() {
        return null;
    }

    @Override
    public void getCities(CallbackCities<List<City>> callbackCities) {
        //определяем откуда тянуть информацию/ Из бд или из сети
        CitiesDataStore citiesDataStore = citiesDataStoreFactory.create();

        //Для удобства обработки CallBack'ов были
        // введены собственные исключения
        try {
            //Данные обновлены успешно
            callbackCities.onUpdate(
                    citiesDataStore.getCities());
        } catch (EmptyDbException e) {
            //отсутствуют данные для обновления, или прочие ошибки
            // (В идеале уведомления с подробностями)
            callbackCities.isEmpty();
        } catch (NetworkConnectionException e) {
            //Отображаем не обновленные данные
            callbackCities.onOldFromDb(e.getOldCitiesInfo());
        }
    }
}
