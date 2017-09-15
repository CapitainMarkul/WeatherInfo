package com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities.cloudstore;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.DbToViewMap;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.GsonToDbMap;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.GsonFactory;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities.cloudstore.Helper.CloudStoreHelper;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

/**
 * Created by da.pavlov1 on 23.08.2017.
 */

public class CloudCitiesStore extends CloudStoreHelper {
    private GsonFactory gsonFactory = GsonFactory.getInstance();
    private GsonToDbMap gsonToDbMap = GsonToDbMap.getInstance();
    private DbToViewMap dbToViewMap = DbToViewMap.getInstance();

    public Flowable<CityView> getCitiesRx(List<String> cityNames) {
        return weatherClient.getWeatherByCitiesRx(cityNames)
                .map(string -> gsonFactory.createGsonCityModel(string))
                .map(gsonCity -> gsonToDbMap.convertGsonModelToDaoModel(gsonCity))
                .toFlowable(BackpressureStrategy.BUFFER)
                .switchMap(cityWeatherWrapper -> {
                    //set Update weather info in DB
                    App.getExecutorService().execute(() -> dbClient.updateCity(cityWeatherWrapper));
                    return Flowable.just(dbToViewMap.convertDbModelToViewModel(cityWeatherWrapper.getCityDb(), cityWeatherWrapper.getWeathers(), true));
                });
    }
}
