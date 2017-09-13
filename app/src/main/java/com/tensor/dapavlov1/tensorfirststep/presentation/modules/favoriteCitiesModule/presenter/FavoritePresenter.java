package com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.presenter;

import com.tensor.dapavlov1.tensorfirststep.DisposableManager;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.view.activity.AddCityActivity;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.BasePresenter;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.Wrapper.ResultWrapper;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract.FavoriteCityInteractorPresenterContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract.FavoriteCityRouterPresenterContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.interactor.FavoriteCityInteractor;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.router.FavoriteToAddCityRouter;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.view.activity.FavoriteActivity;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyDbException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.NetworkConnectException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.deleteobservable.DelObserver;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class FavoritePresenter extends BasePresenter<FavoriteActivity>
        implements FavoriteCityInteractorPresenterContract.Presenter, DelObserver {

    private List<CityView> cachedCitiesView;
    private boolean isLoading = false;

    private FavoriteCityInteractorPresenterContract.Interactor interactor = new FavoriteCityInteractor();
    private FavoriteCityRouterPresenterContract.Router router = new FavoriteToAddCityRouter();

    public FavoritePresenter() {
        cachedCitiesView = new ArrayList<>();
    }

    public void updateWeathers() {
        isLoading = false;
        cachedCitiesView.clear();
        activity.getBinding().setIsLoading(true);

        interactor.setListener(this);
        interactor.obtainCitiesWeather();
    }

    public void switchActivity() {
        router.goToActivity(activity, AddCityActivity.class);
    }

    public void deleteCity(CityView city) {
        DbClient.getInstance().subscribe(this);
        interactor.delCityFromDb(city);
    }

    public void showCachedCities() {
//        List<CityView> cachedCities = citiesDataRepository.getCacheCitiesView();
        if (cachedCitiesView != null && !cachedCitiesView.isEmpty()) {
            activity.setItems(cachedCitiesView);
        }
        activity.getBinding().setCities(cachedCitiesView);
    }

    public boolean isLoadingComplete() {
        return isLoading;
    }

    // FIXME: 13.09.2017 Требуется упростить логику
    public void showCitiesView(CityView city) {
        if (activity != null) {
            //showCity
            activity.setItemInAdapter(city);
            activity.getBinding().setCityView(city);

            //cached
            isLoading = true;
            cachedCitiesView.add(city);

            activity.getBinding().setIsLoading(false);
        }
    }

    private void showEmptyCard() {
        isLoading = true;
        activity.getBinding().setIsLoading(false);

        activity.getBinding().setCities(null);
        activity.getBinding().setCityView(null);
    }

    @Override
    public void onObtainCitiesWeather(ResultWrapper<Flowable<CityView>> cityRx) {
        Exception exception = cityRx.getError();
        Flowable<CityView> observable = cityRx.getData();

        if (observable != null) {
            DisposableManager.getInstance().addDisposable(
                    FavoriteActivity.ID_POOL_COMPOSITE_DISPOSABLE,
                    observable
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    this::showCitiesView,
                                    e -> {
                                        activity.showMessage(R.string.unknown_error);
                                        showEmptyCard();
                                    },
                                    () -> {
                                        //Эти сообщения показываем после того, как все данные загрузились
                                        if (exception == null) {
                                            activity.showMessage(R.string.activity_favorite_update_success);
                                        } else {
                                            if (exception instanceof NetworkConnectException) {
                                                activity.showMessage(R.string.str_error_connect_to_internet);
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
            cachedCitiesView.remove(deletedCity);
            activity.delCityFromAdapter(deletedCity);
        }
        DbClient.getInstance().unSubscribe(this);
    }
}