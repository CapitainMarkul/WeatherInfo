package com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.interactor;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.core.utils.RepositoryLogic;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.facade.FacadeMap;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.network.exceptions.EmptyResponseException;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.network.exceptions.NetworkConnectException;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.service.WeatherService;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.CommonInteractor;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.Wrapper.ResultWrapper;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract.FavoriteInteractorPresenterContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;

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
        List<String> cityNames = weatherService.getDbService().getCitiesName();

        Observable<CityView> network = weatherService.getWeatherService().getWeatherByCitiesRx(cityNames)
                .map(json -> {

                    CityView city = FacadeMap.jsonToVM(json);
//                    set Update weather info in DB
                    App.getExecutorService().execute(() ->
                            weatherService.getDbService().updateCity(city.getName(), city.getWeatherViews()));
                    return city;
                });

        // TODO: 26.09.2017 loadRx()! Проверить обновление на главном экране 
        Observable<CityView> disk = weatherService.getDbService().loadAllCitiesViewRx().toObservable();

        Observable<CityView> observable = RepositoryLogic.loadNetworkPriority(disk, network);

        ResultWrapper<Flowable<CityView>> result;

        if (observable == disk) {
            result = new ResultWrapper<>(observable.toFlowable(BackpressureStrategy.BUFFER), new NetworkConnectException());
        } else {
            result = new ResultWrapper<>(observable.toFlowable(BackpressureStrategy.BUFFER), null);
        }

        getListener().onObtainCitiesWeather(result);
    }

    @Override
    public void delCityFromDb(CityView city) {
        weatherService.getDbService().deleteCity(city);
    }
}
