package com.tensor.dapavlov1.tensorfirststep.provider.repository;

import android.support.annotation.Nullable;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.provider.Callback;
import com.tensor.dapavlov1.tensorfirststep.provider.DataProvider;

import java.io.IOException;
import java.util.List;

/**
 * Created by da.pavlov1 on 22.08.2017.
 */

public class ApiCityDataStore implements CityDataStore {
    @Nullable
    @Override
    public List<City> getCities(List<City> citiesFromDb) {

        try {
            return DataProvider.getInstance().updateCityInfo(citiesFromDb);
        } catch (IOException e) {
            return null;
        }
//        try {
//            //Обновляем информацию о погоде
//            DataProvider.getInstance().updateCityInfo(
//                    citiesFromDb,
//                    new Callback<List<City>>() {
//                        //обновляем погоду
//                        @Override
//                        public void onSuccess(List<City> result) {
//                            //кешируем результат
//                            cachedInfo(result);
//                            presenterCallBack.onSuccess();
//                        }
//                    });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
