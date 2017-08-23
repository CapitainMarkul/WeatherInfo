package com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.presenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.tensor.dapavlov1.tensorfirststep.data.daomodels.ModelCityWeather;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.BasePresenter;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.PresenterCallBack;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.view.activity.AddCityActivity;
import com.tensor.dapavlov1.tensorfirststep.provider.Callback;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.TempCity;
import com.tensor.dapavlov1.tensorfirststep.provider.DataProvider;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */


public class AddCityPresenter extends BasePresenter<AddCityActivity> {
    //    private AddCityActivity activity;
    private City cachedCity;
    private boolean isFavorite;
    private boolean isRefresh = false;
    public final static String CITY_VIEW_MODEL = "city_view_model";
    public final static String CITY_TEMP_DB_MODEL = "city_db_model";
    //для связи с UI
    private Handler sendMessageToUi;

    public void resumePresenter(Bundle saveDataBundle) {
        cachedCity = (City) saveDataBundle.get(CITY_VIEW_MODEL);
        TempCity.getInstance().setModelCityWeather((ModelCityWeather) saveDataBundle.get(CITY_TEMP_DB_MODEL));
        showInformation(cachedCity);
    }

    public Bundle saveDate(Bundle outState){
        //объект для отображения
        outState.putParcelable(CITY_VIEW_MODEL, cachedCity);
        //объект для работы с Бд (Удаления/Добавления)
        outState.putParcelable(CITY_TEMP_DB_MODEL, TempCity.getInstance().getModelCityWeather());
        return outState;
    }




    public AddCityPresenter() {
        sendMessageToUi = new Handler(Looper.getMainLooper());
    }

    private PresenterCallBack presenterCallBack = new PresenterCallBack() {
        @Override
        public void onSuccess() {
            //            Показываем результат пользователю
            sendMessageToUi.post(new Runnable() {
                @Override
                public void run() {
                    activity.cityIsFavorite(isFavorite);
                    showInformation(cachedCity);
                    hideViewLoading();
                }
            });
        }

        @Override
        public void onNothingFind() {
            sendMessageToUi.post(new Runnable() {
                @Override
                public void run() {
                    activity.showWeatherCardNothingFind();
                    hideViewLoading();
                }
            });
        }
    };

    private void cachedInfo(City city, boolean isFavorite) {
        isRefresh = false;
        cachedCity = city;
        this.isFavorite = isFavorite;
    }

    public void getWeatherInCity(String fullCityName) {
        isRefresh = true;
        showViewLoading();

        DataProvider.getInstance().getWeathers(
                fullCityName,
                new Callback<City>() {
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
                });
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

    public void checkEndTask() {
        if (isRefresh) {
            showViewLoading();
        } else {
            hideViewLoading();
            if (cachedCity != null) {
                showInformation(cachedCity);
            }
        }
    }

    private void hideViewLoading() {
        activity.hideLoading();
    }

    private void showViewLoading() {
        activity.showLoading();
    }

//    private void showCardWeatherInfo() {
//        activity.showWeatherCardFullInfo();
//    }

    public void showInformation(City city) {
        activity.showWeatherCardFullInfo();
        activity.showInformation(city);
    }

    private void showMessage(int message) {
        activity.showMessage(message);
    }

}
