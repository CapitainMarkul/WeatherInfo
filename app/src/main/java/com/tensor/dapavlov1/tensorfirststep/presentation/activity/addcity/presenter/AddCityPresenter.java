package com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.presenter;

import com.tensor.dapavlov1.tensorfirststep.PresenterCallBack;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.view.activity.AddCityActivity;
import com.tensor.dapavlov1.tensorfirststep.provider.Callback;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.TempCity;
import com.tensor.dapavlov1.tensorfirststep.provider.DataProvider;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */


public class AddCityPresenter {
    private AddCityActivity activity;
    private City cachedCity;
    private boolean isFavorite;
    private boolean isRefresh = false;

    private void cachedInfo(City city, boolean isFavorite) {
        isRefresh = false;
        cachedCity = city;
        this.isFavorite = isFavorite;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public City getCachedCity() {
        return cachedCity;
    }

    private PresenterCallBack presenterCallBack = new PresenterCallBack() {
        @Override
        public void onSuccess() {
            showCityIsFavorite(isFavorite);
//            Показываем результат пользователю
            showCardWeatherInfo();
            showInformation(cachedCity);
            hideViewLoading();
        }

        @Override
        public void onNothingFind() {
            showCardEmpty();
            hideViewLoading();
        }
    };

    public void setActivity(AddCityActivity activity) {
        this.activity = activity;
    }

    public void getWeatherInCity(String fullCityName) {
        isRefresh = true;
        DataProvider.getInstance().getWeathers(new Callback<City>() {
            @Override
            public void onSuccess(City resultCity, boolean isFavorite) {
                if (resultCity != null) {
                    cachedInfo(resultCity, isFavorite);
                    presenterCallBack.onSuccess();
                }
            }

            @Override
            public void onFail() {
                presenterCallBack.onNothingFind();
            }
        }, fullCityName);
    }

    public void addToFavorite() {
        DataProvider.getInstance().addCityToFavorite(
                TempCity.getInstance().getModelCityWeather());
        showMessage(R.string.activity_favorite_add_to_favorite);
    }

    public void deleteFromFavorite() {
        DataProvider.getInstance().deleteCity(
                TempCity.getInstance().getModelCityWeather());
        showMessage(R.string.activity_favorite_del_from_favorite);
    }

    private void hideViewLoading() {
        activity.hideLoading();
    }

    private void showViewLoading() {
        activity.showLoading();
    }

    private void showCardWeatherInfo() {
        activity.showWeatherCardFullInfo();
    }

    private void showCardEmpty() {
        activity.showWeatherCardNothingFind();
    }

    private void showInformation(City city) {
        activity.showInformation(city);
    }

    private void showMessage(int message) {
        activity.showMessage(message);
    }

    private void showCityIsFavorite(Boolean checked) {
        activity.setChecked(checked);
    }
}
