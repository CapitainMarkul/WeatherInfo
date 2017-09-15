package com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.presenter;

import android.content.Intent;

import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.domain.services.syncChangeOtherActivity.receiver.UpdateActivityInfoReceiver;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.BasePresenter;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.view.activity.AddCityActivity;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.Wrapper.ResultWrapper;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract.FavoriteCityInteractorPresenterContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract.FavoriteCityRouterPresenterContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.interactor.FavoriteCityInteractor;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.router.FavoriteToAddCityRouter;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.view.activity.FavoriteActivity;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.viewmodel.FavoriteViewModel;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.client.DbClient;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities.mythrows.EmptyDbException;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities.mythrows.NetworkConnectException;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.deleteobservable.DelObserver;


import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class FavoritePresenter extends BasePresenter<FavoriteViewModel>
        implements FavoriteCityInteractorPresenterContract.Presenter, DelObserver,
        UpdateActivityInfoReceiver.Receiver {
    private FavoriteCityInteractorPresenterContract.Interactor interactor = new FavoriteCityInteractor();
    private FavoriteCityRouterPresenterContract.Router router = new FavoriteToAddCityRouter();

    private static final int INFO_IS_CHANGE_STATE = 1;

    private UpdateActivityInfoReceiver receiver = UpdateActivityInfoReceiver.getInstance();

    public void updateWeathers() {
        getViewModel().setLoading(true);

        interactor.setListener(this);
        interactor.obtainCitiesWeather();
    }

    public void deleteCity(CityView city) {
        DbClient.getInstance().subscribe(this);
        interactor.delCityFromDb(city);
    }

    public void switchActivity() {
        receiver.setReceiver(this);
        router.goToActivity(getActivity().getComponentsActivity(), AddCityActivity.class);
    }

    public void showCitiesView(CityView city) {
        getViewModel().addCityView(city);
        getViewModel().setLoading(false);
    }

    private void showEmptyCard() {
        getViewModel().setLoading(false);
        getViewModel().showEmptyResult();
    }

    @Override
    public void onObtainCitiesWeather(ResultWrapper<Flowable<CityView>> cityRx) {
        Exception exception = cityRx.getError();
        Flowable<CityView> observable = cityRx.getData();

        if (observable != null) {
            getDisposableManager().addDisposable(
                    FavoriteActivity.FAVORITE_CITY_ADAPTER_KEY,
                    observable
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    this::showCitiesView,
                                    e -> {
                                        getViewModel().setSuccessMessage(R.string.unknown_error);
                                        showEmptyCard();
                                    },
                                    () -> {
                                        //Эти сообщения показываем после того, как все данные загрузились
                                        if (exception == null) {
                                            getViewModel().setErrorMessage(R.string.activity_favorite_update_success);
                                        } else {
                                            if (exception instanceof NetworkConnectException) {
                                                getViewModel().setErrorMessage(R.string.str_error_connect_to_internet);
                                            }
                                        }
                                    }));
        } else {
            if (exception instanceof EmptyDbException) {
                showEmptyCard();
            }
        }
    }

    @Override
    public void deleteResult(boolean isSuccess, CityView deletedCity) {
        if (isSuccess) {
            getViewModel().delCityView(deletedCity);
        }
        DbClient.getInstance().unSubscribe(this);
    }

    @Override
    public void onReceiveResult(Intent intent) {
        switch (intent.getIntExtra(AddCityActivity.GET_STATE, 0)) {
            case INFO_IS_CHANGE_STATE: {
                getViewModel().resetAdapter();
                updateWeathers();
                break;
            }
            default:
                break;
        }
        receiver.reset();
    }

    @Override
    public void destroy() {

    }
}