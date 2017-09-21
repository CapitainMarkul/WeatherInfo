package com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities.cloudstore;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.CityDb;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.GsonToViewMap;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.GsonFactory;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.DbClient;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.client.WeatherApiClient;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities.mythrows.EmptyResponseException;

import io.reactivex.Observable;

/**
 * Created by da.pavlov1 on 12.09.2017.
 */

public class CloudCityStore {
    private final WeatherApiClient weatherApiClient;
    private final DbClient dbClient;

    public CloudCityStore(WeatherApiClient weatherApiClient, DbClient dbClient) {
        this.weatherApiClient = weatherApiClient;
        this.dbClient = dbClient;
    }

    public Observable<CityView> getCity(String fullCityName) {
        return weatherApiClient.getWeatherByCityRx(trimCityName(fullCityName))
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

    private static String trimCityName(String fullCityName) {
        if (fullCityName.indexOf(',') != -1) {
            return fullCityName.substring(0, fullCityName.indexOf(','));
        } else {
            return fullCityName;
        }
    }
}
