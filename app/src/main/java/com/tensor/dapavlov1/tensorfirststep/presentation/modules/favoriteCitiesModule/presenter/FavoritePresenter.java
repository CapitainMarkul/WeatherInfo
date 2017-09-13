package com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.presenter;

import android.util.Log;
import android.widget.Toast;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.DisposableManager;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.view.activity.AddCityActivity;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.BasePresenter;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.Wrapper.ResultWrapper;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract.FavoriteCityInteractorPresenterContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.interactor.FavoriteCityInteractor;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.view.activity.FavoriteActivity;
import com.tensor.dapavlov1.tensorfirststep.interfaces.Router;
import com.tensor.dapavlov1.tensorfirststep.provider.callbacks.CityCallback;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.CitiesDataRepository;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyDbException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.NetworkConnectException;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class FavoritePresenter extends BasePresenter<FavoriteActivity>
        implements FavoriteCityInteractorPresenterContract.Presenter {
    private Router router;
    private CitiesDataRepository citiesDataRepository;
    private boolean isLoading = false;

    private FavoriteCityInteractorPresenterContract.Interactor interactor = new FavoriteCityInteractor();

    public FavoritePresenter() {
        citiesDataRepository = new CitiesDataRepository();
    }

    public void updateWeathers() {
        isLoading = false;
        citiesDataRepository.clearCacheCitiesView();
        activity.getBinding().setIsLoading(true);

        interactor.setListener(this);
        interactor.obtainCitiesWeather();

//        activity.getDisposableManager().addDisposable(
//                FavoriteActivity.ID_POOL_COMPOSITE_DISPOSABLE,
//                citiesDataRepository.getCitiesRx()
//                        .getData()
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(
//                                city -> citiesCallback.onUpdate(city),
//                                throwable -> citiesCallback.onComplete(),
//                                () -> citiesCallback.onComplete()));
    }

    public void switchActivity() {
        router.goToActivity(activity, AddCityActivity.class);
    }

    public void deleteCity(String cityName) {
        citiesDataRepository.delete(cityName);
    }

    public void showCachedCities() {
        List<CityView> cachedCities = citiesDataRepository.getCacheCitiesView();
        if (cachedCities != null && !cachedCities.isEmpty()) {
            activity.setItems(cachedCities);
        }
        activity.getBinding().setCities(cachedCities);
    }

    public boolean isLoadingComplete() {
        return isLoading;
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    // FIXME: 13.09.2017 Требуется упростить логику
    public void showCitiesView(CityView city) {
        if (activity != null) {
            //showCity
            activity.setItemInAdapter(city);
            activity.getBinding().setCityView(city);

            //cached
            isLoading = true;
            citiesDataRepository.cacheCityView(city);

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
                    AddCityActivity.ID_POOL_COMPOSITE_DISPOSABLE,
                    observable
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    this::showCitiesView,
                                    e -> activity.showMessage(R.string.unknown_error),
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
}