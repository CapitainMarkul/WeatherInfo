package com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.interfaces.DelCityFromDb;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.MvpInteractor;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.Wrapper.ResultWrapper;

import io.reactivex.Flowable;

/**
 * Created by da.pavlov1 on 12.09.2017.
 */

public interface FavoriteInteractorPresenterContract {
    interface Presenter extends MvpInteractor.LPresenter {
        void onObtainCitiesWeather(ResultWrapper<Flowable<CityView>> cityRx);
    }

    interface Interactor extends MvpInteractor<Presenter>, DelCityFromDb {
        void obtainCitiesWeather();
    }
}