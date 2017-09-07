package com.tensor.dapavlov1.tensorfirststep.presentation.activity.favorite.presenter;

import android.util.Log;
import android.widget.Toast;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.view.activity.AddCityActivity;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.BasePresenter;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.favorite.view.activity.FavoriteActivity;
import com.tensor.dapavlov1.tensorfirststep.interfaces.Router;
import com.tensor.dapavlov1.tensorfirststep.provider.callbacks.CityCallback;
import com.tensor.dapavlov1.tensorfirststep.provider.callbacks.DelCityCallBack;
import com.tensor.dapavlov1.tensorfirststep.provider.common.ConnectorDeleteListener;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.CitiesDataRepository;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows.EmptyDbException;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class FavoritePresenter extends BasePresenter<FavoriteActivity> {
    private Router router;
    private List<CityView> cachedCities = new ArrayList<>();

    private boolean isLoading = false;

    private CityCallback<CityView> citiesCallback = new CityCallback<CityView>() {
        @Override
        public void onUpdate(final CityView result) {
            try {
                if (activity != null) {
                    showCity(result);
                    cachedInfo(result);

                    activity.getBinding().setIsLoading(false);
                }
            } catch (NullPointerException e) {
                Toast.makeText(App.getContext(), App.getContext().getText(R.string.unknown_error), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onOldFromDb(final CityView result) {
            activity.showMessage(R.string.str_error_connect_to_internet);
        }

        @Override
        public void onErrorConnect() {
            activity.showMessage(R.string.str_error_connect_to_internet);
        }

        @Override
        public void isEmpty() {
            isLoading = true;
            Log.e("Visible", String.valueOf(activity.getBinding().cardWeatherDefault.cvDefault.getVisibility()));
            activity.getBinding().setIsLoading(false);
            Log.e("isLoading", String.valueOf(activity.getBinding().getIsLoading()));
            Log.e("City", String.valueOf(activity.getBinding().getCityView()));
            Log.e("Cities", String.valueOf(activity.getBinding().getCities()));
            Log.e("VisibleTotal", String.valueOf(activity.getBinding().cardWeatherDefault.cvDefault.getVisibility()));

//            activity.showMessage(R.string.activity_favorite_update_success);
//            activity.getBinding().setCities(null);
//            activity.getBinding().setCityView(null);
        }

        @Override
        public void onComplete() {
            isLoading = true;
            activity.getBinding().setIsLoading(false);
            activity.showMessage(R.string.activity_favorite_update_success);
        }
    };

    private void cachedInfo(CityView cityView) {
        isLoading = true;
        cachedCities.add(cityView);
    }

    public void updateWeathers() {
        isLoading = false;
        cachedCities.clear();
        activity.getBinding().setIsLoading(true);

        try {
            activity.getDisposableManager().addDisposable(
                    FavoriteActivity.ID_POOL_COMPOSITE_DISPOSABLE,
                    new CitiesDataRepository().getCitiesRx()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    city -> citiesCallback.onUpdate(city),
                                    throwable -> citiesCallback.onComplete(),
                                    () -> citiesCallback.onComplete()));
        } catch (EmptyDbException e) {
            citiesCallback.isEmpty();
        }
    }

    public void switchActivity() {
        router.goToActivity(activity, AddCityActivity.class);
    }

    public void deleteCity(String cityName) {
        new CitiesDataRepository().delete(cityName);
        // FIXME: 07.09.2017 Чистить из кеша нужно только после того, как произошло удаление по факту
        for(CityView item : cachedCities){
            if (item.getName().equals(cityName)){
                cachedCities.remove(item);
                break;
            }
        }
//        cachedCities.remove(cachedCities.indexOf(cachedCities));
    }

    private void showCity(CityView cityView) {
        activity.setItemInAdapter(cityView);
        activity.getBinding().setCityView(cityView);
    }

    public void clearCacheCities() {
        cachedCities.clear();
    }

    public void showCachedCities() {
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
}