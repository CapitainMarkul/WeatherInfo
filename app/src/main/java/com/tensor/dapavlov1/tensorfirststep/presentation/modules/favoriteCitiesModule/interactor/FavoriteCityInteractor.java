package com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.interactor;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.CommonInteractor;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.Wrapper.ResultWrapper;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract.FavoriteInteractorPresenterContract;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.command.cloud.GetWeatherByCitiesCommand;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.command.db.DelCityFromDbCommand;

import io.reactivex.Flowable;

/**
 * Created by da.pavlov1 on 12.09.2017.
 */

public class FavoriteCityInteractor extends CommonInteractor<FavoriteInteractorPresenterContract.Presenter, ResultWrapper<Flowable<CityView>>>
        implements FavoriteInteractorPresenterContract.Interactor {

    @Override
    public void obtainCitiesWeather() {
        getListener().onObtainCitiesWeather(
                executeCommand(new GetWeatherByCitiesCommand()));
    }

    @Override
    public void delCityFromDb(CityView city) {
        executeVoidCommand(new DelCityFromDbCommand(city));
    }
}
