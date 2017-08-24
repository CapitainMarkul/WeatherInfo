package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.ModelCityWeather;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.provider.CallbackCities;
import com.tensor.dapavlov1.tensorfirststep.provider.CallbackCity;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces.CitiesDataStore;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces.CitiesRepository;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.interfaces.CityDataStore;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.CityFoundException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.CityNotFoundException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyDbException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyResponseException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.NetworkConnectException;

import java.util.List;

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
    public void getCity(String fullCityName, CallbackCity<City> callbackCity) {
        try {
            CityDataStore cityDataStore = citiesDataStoreFactory.createCityDataStore();

            try {
                cityDataStore.getCity(fullCityName);
            } catch (EmptyResponseException e) {
                callbackCity.onErrorConnect();
            } catch (CityNotFoundException obj) {
                callbackCity.isNotFavoriteCity(obj.getCity());
            } catch (CityFoundException obj) {
                callbackCity.isFavoriteCity(obj.getCity());
            }
        } catch (NetworkConnectException e) {
            callbackCity.onErrorConnect();
        }
    }

    @Override
    public void getCities(CallbackCities<List<City>> callbackCities) {
        //определяем откуда тянуть информацию/ Из бд или из сети
        CitiesDataStore citiesDataStore = citiesDataStoreFactory.createCitiesDataStore();

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
        } catch (NetworkConnectException e) {
            //Отображаем не обновленные данные
            callbackCities.onOldFromDb(e.getOldCitiesInfo());
        }
    }


}
