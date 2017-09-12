package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.cloudstore.Helper;

import com.tensor.dapavlov1.tensorfirststep.provider.ApiFactory;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;
import com.tensor.dapavlov1.tensorfirststep.provider.client.WeatherApiClient;

/**
 * Created by da.pavlov1 on 12.09.2017.
 */

public abstract class CloudStoreHelper {
    protected DbClient dbClient = DbClient.getInstance();
    protected WeatherApiClient weatherClient = ApiFactory.getInstance().createClientWeatherApi();

    protected static String trimCityName(String fullCityName) {
        if (fullCityName.indexOf(',') != -1) {
            return fullCityName.substring(0, fullCityName.indexOf(','));
        } else {
            return fullCityName;
        }
    }
}
