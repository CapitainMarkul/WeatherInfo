package com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.interactor;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.contract.AddCityInteractorPresenterContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.CommonInteractor;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.Wrapper.ResultWrapper;
import com.tensor.dapavlov1.tensorfirststep.provider.command.cloud.GetWeatherByCityCommand;
import com.tensor.dapavlov1.tensorfirststep.provider.command.db.AddCityInDbCommand;
import com.tensor.dapavlov1.tensorfirststep.provider.command.db.DelCityFromDbCommand;

import io.reactivex.Observable;

/**
 * Created by da.pavlov1 on 12.09.2017.
 */

public class AddCityInteractor extends CommonInteractor<AddCityInteractorPresenterContract.Presenter, ResultWrapper<Observable<CityView>>>
        implements AddCityInteractorPresenterContract.Interactor {

    @Override
    public void obtainCityWeather(String fullCityName) {
        getListener().onObtainCityWeather(
                executeCommand(new GetWeatherByCityCommand(fullCityName)));
    }

    @Override
    public void addCityInDb(CityView city) {
        executeVoidCommand(new AddCityInDbCommand(city));
    }

    @Override
    public void delCityFromDb(CityView city) {
        executeVoidCommand(new DelCityFromDbCommand(city));
    }
}