package com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.presenter;

import com.tensor.dapavlov1.tensorfirststep.DisposableManager;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.ActivityComponents;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.BasePresenter;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.contract.AddCityInteractorPresenterContract;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.interactor.AddCityInteractor;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.view.activity.AddCityActivity;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.viewmodel.AddCityViewModel;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.interactor.Wrapper.ResultWrapper;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.provider.client.DbClient;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.NetworkConnectException;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.deleteobservable.DelObserver;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */


public class AddCityPresenter extends BasePresenter<AddCityViewModel>
        implements DelObserver, AddCityInteractorPresenterContract.Presenter {

    //Temp Answer
    // TODO: 12.09.2017 В будущем -  Dagger 2
    private AddCityInteractorPresenterContract.Interactor interactor = new AddCityInteractor();
//    private CityView cachedCityView;

    //для созранения состояния при ConfigChange
//    private boolean isLoading = false;

    private final static String CITY_VIEW_MODEL = "city_view_model";

    @Override
    public void attachView(AddCityViewModel viewModel, ActivityComponents activity) {
        super.attachView(viewModel, activity);
        interactor.setListener(this);
    }

    @Override
    public void detachView() {
        interactor.setListener(null);
        super.detachView();
    }

    @Override
    public void destroy() {

    }

    public void getWeatherInCity(final String fullCityName) {
        getViewModel().setLoading(true);
        getViewModel().setFirstLaunch(false);

        interactor.obtainCityWeather(fullCityName);
    }

    private void showMessage(int message) {
        getViewModel().setSuccessMessage(message);
    }

    private void showCityView(CityView city) {
        getViewModel().setCityView(city);
        getViewModel().setLoading(false);
        getViewModel().getCity().setFavorite((city.isFavorite()));
    }

    private void showErrorView(int errorMessage) {
        getViewModel().setErrorMessage(errorMessage);
        getViewModel().setCityView(null);
        getViewModel().setLoading(false);
    }


    @Override
    public void onObtainCityWeather(ResultWrapper<Observable<CityView>> cityRx) {
        Exception exception = cityRx.getError();
        Observable<CityView> observable = cityRx.getData();

        if (exception == null && observable != null) {
            DisposableManager.getInstance().addDisposable(
                    AddCityActivity.DISPOSABLE_POOL_KEY,
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

    public void onFavoriteClick() {
        if (!getViewModel().getCity().isFavorite()) {
            addToFavorite(getViewModel().getCity());
        } else {
            deleteFromFavorite(getViewModel().getCity());
        }
    }

    private void addToFavorite(CityView city) {
        interactor.addCityInDb(city);
        getViewModel().setFavorite(true);
        showMessage(R.string.activity_favorite_add_to_favorite);
    }

    private void deleteFromFavorite(CityView city) {
        DbClient.getInstance().subscribe(this);
        interactor.delCityFromDb(city);
    }

    @Override
    public void deleteResult(boolean isSuccess, CityView deletedCity) {
        if (isSuccess) {
            getViewModel().setFavorite(false);
            showMessage(R.string.activity_favorite_del_from_favorite);
        } else {
            showMessage(R.string.unknown_error);
        }
        DbClient.getInstance().unSubscribe(this);
    }
}
