package com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.interactor;

import com.tensor.dapavlov1.tensorfirststep.core.utils.RepositoryLogic;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities.mythrows.NetworkConnectException;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.service.WeatherService;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.contract.AddCityInteractorPresenterContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.CommonInteractor;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.Wrapper.ResultWrapper;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by da.pavlov1 on 12.09.2017.
 */

public class AddCityInteractor extends CommonInteractor<AddCityInteractorPresenterContract.Presenter, ResultWrapper<Observable<CityView>>>
        implements AddCityInteractorPresenterContract.Interactor {

    private final WeatherService weatherService;

    @Inject
    public AddCityInteractor(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Override
    public void obtainCityWeather(String fullCityName) {
        //Было1
//        getListener().onObtainCityWeather(
//                executeCommand(new GetWeatherByCityCommand(fullCityName)));
        //Было2
//        getListener().onObtainCityWeather(
//                citiesDataRepository.getCityRx(fullCityName));
        //Стало
        Observable<String> network = weatherService.getWeatherService().getWeatherByCityRx(fullCityName);
        Observable<String> observable = RepositoryLogic.loadNetworkPriority(null, network);

        ResultWrapper<Observable<String>> result;

        if (observable.isEmpty().blockingGet()) {
            result = new ResultWrapper<>(null, new NetworkConnectException());
        } else {
            result = new ResultWrapper<>(observable, null);
        }

        getListener().onObtainCityWeather(result);
    }

    @Override
    public void addCityInDb(CityView city) {
        weatherService.getDbService().addInDataBase(city);
//        executeVoidCommand(new AddCityInDbCommand(city));
    }

    @Override
    public void delCityFromDb(CityView city) {
        weatherService.getDbService().deleteCity(city);
    }
}
