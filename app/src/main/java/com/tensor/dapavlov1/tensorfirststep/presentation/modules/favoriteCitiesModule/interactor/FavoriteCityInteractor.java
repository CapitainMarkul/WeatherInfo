package com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.interactor;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.core.utils.RepositoryLogic;
import com.tensor.dapavlov1.tensorfirststep.data.mappers.facade.FacadeMap;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.network.exceptions.NetworkConnectException;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.service.WeatherService;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.CommonInteractor;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.Wrapper.ResultWrapper;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract.FavoriteInteractorPresenterContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.view.activity.FavoriteActivity;

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
    public void obtainCitiesWeather(FavoriteActivity.WaysUpdatePriority waysUpdatePriority) {
        Observable<CityView> disk;
        Observable<CityView> network;
        ResultWrapper<Flowable<CityView>> result;

        switch (waysUpdatePriority) {
            case LOCAL: {
                disk = weatherService.getDbService().loadAllCitiesViewRx();
                result = new ResultWrapper<>(disk.toFlowable(BackpressureStrategy.BUFFER), null);
                getListener().onObtainCitiesWeather(result);
                break;
            }
            case NETWORK: {
                List<String> cityNames = weatherService.getDbService().getCitiesName();

                disk = weatherService.getDbService().loadAllCitiesViewRx();

                network = weatherService.getWeatherService().getWeatherByCitiesRx(cityNames)
                        .map(FacadeMap::jsonToVM)
                        .doOnNext(city -> App.getExecutorService().execute(() ->
                                weatherService.getDbService().updateCity(city.getName(), city.getWeatherViews())));

                Observable<CityView> observable = RepositoryLogic.loadNetworkPriority(disk, network);

                // TODO: 27.09.2017 Избавиться от Flowable?
                if (observable == disk) {
                    result = new ResultWrapper<>(observable.toFlowable(BackpressureStrategy.BUFFER), new NetworkConnectException());
                } else {
                    result = new ResultWrapper<>(observable.toFlowable(BackpressureStrategy.BUFFER), null);
                }

//        ResultWrapper<Observable<CityView>> result;
//
//        if (observable == disk) {
//            result = new ResultWrapper<>(observable, new NetworkConnectException());
//        } else {
//            result = new ResultWrapper<>(observable, null);
//        }

                getListener().onObtainCitiesWeather(result);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void delCityFromDb(CityView city) {
        weatherService.getDbService().deleteCity(city);
    }
}
