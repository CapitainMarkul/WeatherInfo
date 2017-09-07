package com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.presenter;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.BasePresenter;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.view.activity.AddCityActivity;
import com.tensor.dapavlov1.tensorfirststep.provider.callbacks.CityCallback;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.CitiesDataRepository;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */


public class AddCityPresenter extends BasePresenter<AddCityActivity> {
    private CityView cachedCityView;
    //для созранения состояния при ConfigChange
    private boolean isLoading = false;

    private final static String CITY_VIEW_MODEL = "city_view_model";

    private void cityIsFavorite(boolean isFavorite) {
        cachedCityView.setFavorite(isFavorite);
    }

    public void resumePresenter(Bundle saveDataBundle) {
        cachedCityView = (CityView) saveDataBundle.get(CITY_VIEW_MODEL);
        showInformation(cachedCityView);
    }

    public Bundle saveData(Bundle outState) {
        //объект для отображения
        //ситуация с TerminateApp, презентер пересоздается, и не имеет кешированной информации
        if (cachedCityView != null) {
            outState.putParcelable(CITY_VIEW_MODEL, cachedCityView);
        }
        return outState;
    }

    private CityCallback<CityView> cityCallback = new CityCallback<CityView>() {
        @Override
        public void isFavoriteCity(final CityView result) {
            activity.isFavoriteCity(true);
            cachedInfo(result);
            showInformation(cachedCityView);
            stopAnimation();
        }

        @Override
        public void isNotFavoriteCity(final CityView result) {
            activity.isFavoriteCity(false);
            cachedInfo(result);
            showInformation(cachedCityView);
            stopAnimation();
        }

        @Override
        public void onErrorConnect() {
            activity.showMessage(R.string.str_error_connect_to_internet);
            activity.getBinding().setIsLoading(false);
            stopAnimation();
        }

        @Override
        public void isEmpty() {
            isLoading = false;
            showInformation(null);
            activity.getBinding().setIsLoading(false);
            stopAnimation();
        }

        @Override
        public void apiKeyError() {
            // TODO: 04.09.2017 Можно сделать, если потребуется
        }
    };

    public void clearInputText() {
        activity.clearInputText();
    }

    private void cachedInfo(CityView cityView) {
        isLoading = false;
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

        new CitiesDataRepository().getCity(fullCityName, cityCallback);
    }

    private void addToFavorite() {
        new CitiesDataRepository().add(cachedCityView);
        cityIsFavorite(true);
        showMessage(R.string.activity_favorite_add_to_favorite);
    }

    private void deleteFromFavorite() {
        new CitiesDataRepository().delete(cachedCityView.getName());
        cityIsFavorite(false);
        showMessage(R.string.activity_favorite_del_from_favorite);
    }

    public void checkEndTask() {
        if (isLoading) {
            activity.getBinding().setIsLoading(true);
        } else {
            showInformation(cachedCityView);
        }
    }

    public void showInformation(CityView cityView) {
        activity.getBinding().setCityView(cityView);
        activity.getBinding().setIsLoading(false);
        if (cityView != null) {
            activity.refreshWeathers(cachedCityView.getWeatherViews());
        }
    }

    private void showMessage(int message) {
        activity.showMessage(message);
    }

    public void onFavoriteClick() {
        if (activity.isCheckedNow()) {
            addToFavorite();
        } else {
            deleteFromFavorite();
        }
    }
}
