package com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.interactor;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.service.WeatherService;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.CommonInteractor;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.Wrapper.ResultWrapper;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract.FavoriteInteractorPresenterContract;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.command.cloud.GetWeatherByCitiesCommand;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.db.command.DelCityFromDbCommand;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by da.pavlov1 on 12.09.2017.
 */

public class FavoriteCityInteractor extends CommonInteractor<FavoriteInteractorPresenterContract.Presenter, ResultWrapper<Flowable<CityView>>>
        implements FavoriteInteractorPresenterContract.Interactor {

    private final WeatherService weatherService;

    @Inject
    public FavoriteCityInteractor(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Override
    public void obtainCitiesWeather() {
        getListener().onObtainCitiesWeather(
                executeCommand(new GetWeatherByCitiesCommand()));
    }

    @Override
    public void delCityFromDb(CityView city) {
        weatherService.getDbService().deleteCity(city);
//        executeVoidCommand(new DelCityFromDbCommand(city));
    }
}
