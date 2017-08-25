package com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.presenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.data.daomodels.ModelCityWeather;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.BasePresenter;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.view.activity.AddCityActivity;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.TempCity;
import com.tensor.dapavlov1.tensorfirststep.provider.callbacks.CallbackCity;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.CitiesDataRepository;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */


public class AddCityPresenter extends BasePresenter<AddCityActivity> {
    private City cachedCity;
    private boolean isRefresh = false;
    public final static String CITY_VIEW_MODEL = "city_view_model";
    public final static String CITY_TEMP_DB_MODEL = "city_db_model";

    //для связи с UI
    private Handler sendMessageToUi;

    public void resumePresenter(Bundle saveDataBundle) {
        cachedCity = (City) saveDataBundle.get(CITY_VIEW_MODEL);
        if (cachedCity != null) {
            TempCity.getInstance().setModelCityWeather(
                    (ModelCityWeather) saveDataBundle.get(CITY_TEMP_DB_MODEL));
            showInformation(cachedCity);
        } else {
            activity.showWeatherCardNothingFind();
        }
    }

    public Bundle saveData(Bundle outState) {
        //объект для отображения
        //ситуация с TerminateApp, презентер пересоздается, и не имеет кешированной информации
        if (cachedCity != null) {
            outState.putParcelable(CITY_VIEW_MODEL, cachedCity);
        }
        //объект для работы с Бд (Удаления/Добавления)
        outState.putParcelable(CITY_TEMP_DB_MODEL, TempCity.getInstance().getModelCityWeather());
        return outState;
    }

    public AddCityPresenter() {
        sendMessageToUi = new Handler(Looper.getMainLooper());
    }

    private CallbackCity<City> cityCallback = new CallbackCity<City>() {
        @Override
        public void isFavoriteCity(final City result) {
            sendMessageToUi.post(new Runnable() {
                @Override
                public void run() {
                    activity.cityIsFavorite(true);
                    cachedInfo(result);
                    showInformation(cachedCity);
                    hideViewLoading();
                }
            });
        }

        @Override
        public void isNotFavoriteCity(final City result) {
            sendMessageToUi.post(new Runnable() {
                @Override
                public void run() {
                    activity.cityIsFavorite(false);
                    cachedInfo(result);
                    showInformation(result);
                    hideViewLoading();
                }
            });
        }

        @Override
        public void onErrorConnect() {
            sendMessageToUi.post(new Runnable() {
                @Override
                public void run() {
                    activity.showWeatherCardNothingFind();
                    activity.showMessage(R.string.str_error_connect_to_internet);
                    hideViewLoading();
                }
            });
        }

        @Override
        public void isEmpty() {
            sendMessageToUi.post(new Runnable() {
                @Override
                public void run() {
                    isRefresh = false;
                    activity.showWeatherCardNothingFind();
                    hideViewLoading();
                }
            });
        }
    };

    public void clearInputText(){
        activity.clearInputText();
    }

    private void cachedInfo(City city) {
        isRefresh = false;
        cachedCity = city;
    }

    public void getWeatherInCity(final String fullCityName) {
        isRefresh = true;
        showViewLoading();

        App.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                new CitiesDataRepository().getCity(fullCityName, cityCallback);
            }
        });
    }

    public void onClickToFavorite(){
        if(activity.isCheckedNow()){
            deleteFromFavorite();
        } else {
            addToFavorite();
        }
    }

    private void addToFavorite() {
        new CitiesDataRepository().add(
                TempCity.getInstance().getModelCityWeather());
        showMessage(R.string.activity_favorite_add_to_favorite);
    }

    private void deleteFromFavorite() {
        new CitiesDataRepository().delete(
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

    public void showInformation(City city) {
        activity.showWeatherCardFullInfo();
        activity.showInformation(city);
    }

    private void showMessage(int message) {
        activity.showMessage(message);
    }

}
