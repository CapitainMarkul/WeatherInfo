package com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.contract;

import com.tensor.dapavlov1.tensorfirststep.interfaces.AddCityInDb;
import com.tensor.dapavlov1.tensorfirststep.interfaces.DelCityFromDb;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.MvpInteractor;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.Wrapper.ResultWrapper;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by da.pavlov1 on 12.09.2017.
 */

public interface AddCityInteractorPresenterContract {
    interface Presenter extends MvpInteractor.LPresenter {
        void onObtainCityWeather(ResultWrapper<Observable<String>> cityRx);
        void onObtainPlaces(ResultWrapper<Observable<String>> places);
    }

    interface Interactor extends MvpInteractor<Presenter>, DelCityFromDb, AddCityInDb {
        void obtainCityWeather(String fullCityName);
        void obtainPlaces(String inputText);
    }
}
