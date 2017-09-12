package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.cloudstore;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDb;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.GsonToViewMap;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.provider.GsonFactory;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.cloudstore.Helper.CloudStoreHelper;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyResponseException;

import io.reactivex.Observable;

/**
 * Created by da.pavlov1 on 12.09.2017.
 */

public class CloudCityStore extends CloudStoreHelper {
    public Observable<CityView> getCity(String fullCityName) throws EmptyResponseException {
        return weatherClient.getWeatherByCityRx(trimCityName(fullCityName))
                .map(response -> {
                    if (response == null || response.equals("")) {
                        throw new EmptyResponseException();
                    }
                    return response;
                })
                .map(string -> GsonFactory.getInstance().createGsonCityModel(string))
                .map(cityGson -> GsonToViewMap.getInstance().convertGsonToViewModel(cityGson))
                .map(viewCity -> {
                    CityDb cityDb = dbClient.searchCity(viewCity.getName());
                    if (cityDb == null) {
                        viewCity.setFavorite(false);
                        return viewCity;
                    }
                    viewCity.setFavorite(true);
                    return viewCity;
                });
    }
}
