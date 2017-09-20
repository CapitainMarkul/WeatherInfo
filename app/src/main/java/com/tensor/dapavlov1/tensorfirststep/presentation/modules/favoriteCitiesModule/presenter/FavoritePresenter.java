package com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.domain.services.receivers.ReceiverWithAction;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.ActivityComponents;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.BasePresenter;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.view.activity.AddCityActivity;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.Wrapper.ResultWrapper;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract.FavoriteInteractorPresenterContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract.FavoriteRouterPresenterContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract.FavoriteViewModelContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.view.activity.FavoriteActivity;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.viewmodel.FavoriteViewModel;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.client.DbClient;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities.mythrows.EmptyDbException;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.cities.mythrows.NetworkConnectException;
import com.tensor.dapavlov1.tensorfirststep.domain.provider.repository.deleteobservable.DelObserver;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class FavoritePresenter extends BasePresenter<FavoriteViewModelContract.ViewModel>
        implements DelObserver,
        FavoriteInteractorPresenterContract.Presenter,  //для приема данных из Интерактора
        FavoriteViewModelContract.Presenter             //для отправки данных в интерактор
{

    public final static String ADD_NEW_CITY_ACTION = FavoritePresenter.class.getSimpleName() + ".ACTION.ADD_NEW_CITY";
    public final static String WEATHER_SYNC_ACTION = FavoritePresenter.class.getSimpleName() + ".ACTION.WEATHER_SYNC_ACTION";

    private FavoriteInteractorPresenterContract.Interactor interactor;
    private FavoriteRouterPresenterContract.Router router;

    private boolean isSwitchToAddNewCity = false;

    private static final int INFO_IS_CHANGE_STATE = 1;

    @Inject
    public FavoritePresenter(FavoriteInteractorPresenterContract.Interactor interactor,
                             FavoriteRouterPresenterContract.Router router) {
        this.interactor = interactor;
        this.router = router;
    }

    @Override
    public void attachView(FavoriteViewModelContract.ViewModel viewModel, ActivityComponents activityComponents) {
        super.attachView(viewModel, activityComponents);
        registerReceivers();
    }

    @Override
    public void detachView() {
        if (!isSwitchToAddNewCity) {
//            //Если будем переходить на другой экран, не на добавление нового города, то выполним отписку
//            //Если выполнять отписку каждый раз, то результатов мы не получаем
            unregisterReceivers();
        }// TODO: 20.09.2017 разобраться с отпиской
//        unregisterReceivers();
        super.detachView();
    }

    public void updateWeathers() {
        getViewModel().setLoading(true);

        interactor.setListener(this);
        interactor.obtainCitiesWeather();
    }

    public void deleteCity(CityView city) {
        DbClient.getInstance().subscribe(this);
        interactor.delCityFromDb(city);
    }

    @Override
    public void switchActivity() {
        isSwitchToAddNewCity = true;
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

    private BroadcastReceiver addNewCityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getIntExtra(AddCityActivity.GET_STATE, 0)) {
                case AddCityActivity.INFO_IS_CHANGE_STATE: {
                    getViewModel().resetAdapter();
                    updateWeathers();
                    break;
                }
                default:
                    break;
            }
        }
    };

    private BroadcastReceiver weatherSyncReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateWeathers();
        }
    };

    private void registerReceivers() {
        for (ReceiverWithAction item : getReceivers()) {
//            getActivity().getComponentsActivity().registerReceiver(item.getReceiver(), item.getIntentFilter());
            App.get().registerReceiver(item.getReceiver(), item.getIntentFilter());
        }
    }

    private void unregisterReceivers() {
        for (ReceiverWithAction item : getReceivers()) {
//            getActivity().getComponentsActivity().unregisterReceiver(item.getReceiver());
            App.get().unregisterReceiver(item.getReceiver());
        }
    }

    private List<ReceiverWithAction> getReceivers() {
        List<ReceiverWithAction> receivers = new ArrayList<>();
        receivers.add(new ReceiverWithAction(addNewCityReceiver, new IntentFilter(ADD_NEW_CITY_ACTION)));
        receivers.add(new ReceiverWithAction(weatherSyncReceiver, new IntentFilter(WEATHER_SYNC_ACTION)));
        return receivers;
    }

    @Override
    public void destroy() {

    }
}