package com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.presenter;

import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.DisposableManager;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.BasePresenter;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.contract.AddCityInteractorPresenterContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.interactor.AddCityInteractor;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.view.activity.AddCityActivity;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.Wrapper.ResultWrapper;
import com.tensor.dapavlov1.tensorfirststep.provider.callbacks.CityCallback;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.CitiesDataRepository;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyDbException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyResponseException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.NetworkConnectException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.deleteobservable.DelObserver;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */


public class AddCityPresenter extends BasePresenter<AddCityActivity>
        implements DelObserver, AddCityInteractorPresenterContract.Presenter {

    //Temp Answer
    // TODO: 12.09.2017 В будущем -  Dagger 2
    private AddCityInteractorPresenterContract.Interactor interactor = new AddCityInteractor();
    private CityView cachedCityView;
    //для созранения состояния при ConfigChange
    private boolean isLoading = false;

    private final static String CITY_VIEW_MODEL = "city_view_model";

    private void cityIsFavorite(boolean isFavorite) {
        cachedCityView.setFavorite(isFavorite);
    }

    public void resumePresenter(Bundle saveDataBundle) {
        cachedCityView = (CityView) saveDataBundle.get(CITY_VIEW_MODEL);
        showCityView(cachedCityView);
    }

    public Bundle saveData(Bundle outState) {
        //объект для отображения
        //ситуация с TerminateApp, презентер пересоздается, и не имеет кешированной информации
        if (cachedCityView != null) {
            outState.putParcelable(CITY_VIEW_MODEL, cachedCityView);
        }
        return outState;
    }

    public void clearInputText() {
        activity.clearInputText();
    }

    private void cachedInfo(CityView cityView) {
        cachedCityView = cityView;
    }

    private void startAnimation() {
        Animation anim = AnimationUtils.loadAnimation(App.getContext(), R.anim.alpha);
        activity.getBinding().cvSearching.tvSearching.startAnimation(anim);
    }

    private void stopAnimation() {
        activity.getBinding().cvSearching.tvSearching.clearAnimation();
    }

    public void getWeatherInCity(final String fullCityName) {
        isLoading = true;
        activity.getBinding().setIsLoading(true);
        startAnimation();

        interactor.setListener(this);
        interactor.obtainCityWeather(fullCityName);
    }

    private void addToFavorite() {
        interactor.addCityInDb(cachedCityView);
        cityIsFavorite(true);
        showMessage(R.string.activity_favorite_add_to_favorite);
    }

    private void deleteFromFavorite() {
        interactor.delCityFromDb(cachedCityView);
    }

    public void checkEndTask() {
        if (isLoading) {
            activity.getBinding().setIsLoading(true);
        } else {
            if (cachedCityView != null) {
                showCityView(cachedCityView);
            }
        }
    }

    private void showMessage(int message) {
        activity.showMessage(message);
    }

    public void onFavoriteClick() {
        if (activity.isCheckedNow()) {
            addToFavorite();
        } else {
            DbClient.getInstance().subscribe(this);
            deleteFromFavorite();
        }
    }

    private void showCityView(CityView city) {
        isLoading = false;
        cachedInfo(city);
        activity.getBinding().setCityView(city);
        activity.getBinding().setIsLoading(false);
        activity.isFavoriteCity(city.isFavorite());

        activity.refreshWeathers(cachedCityView.getWeatherViews());
        stopAnimation();
    }

    private void showErrorView(int errorMessage) {
        isLoading = false;
        activity.showMessage(errorMessage);
        activity.getBinding().setCityView(null);
        activity.getBinding().setIsLoading(false);
        stopAnimation();
    }


    @Override
    public void onObtainCityWeather(ResultWrapper<Observable<CityView>> cityRx) {
        Exception exception = cityRx.getError();
        Observable<CityView> observable = cityRx.getData();

        if (exception == null && observable != null) {
            DisposableManager.getInstance().addDisposable(
                    AddCityActivity.ID_POOL_COMPOSITE_DISPOSABLE,
                    observable
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    this::showCityView,
                                    e -> showErrorView(R.string.unknown_error)));
        } else {
            if (exception instanceof NetworkConnectException) {
                showErrorView(R.string.str_error_connect_to_internet);
            } else {
                showErrorView(R.string.unknown_error);
            }
        }
    }

    @Override
    public void deleteResult(boolean isSuccess, CityView deletedCity) {
        if (isSuccess) {
            cityIsFavorite(!isSuccess);
            showMessage(R.string.activity_favorite_del_from_favorite);
        } else {
            showMessage(R.string.unknown_error);
        }
        DbClient.getInstance().unSubscribe(this);
    }
}
