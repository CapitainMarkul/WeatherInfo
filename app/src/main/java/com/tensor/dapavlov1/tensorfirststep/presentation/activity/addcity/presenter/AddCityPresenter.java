package com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.presenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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
    //для созранения состояния при ConfigChange
    private boolean isLoading = false;

    public final static String CITY_VIEW_MODEL = "city_view_model";
    public final static String CITY_TEMP_DB_MODEL = "city_db_model";

    //для связи с UI
    private Handler sendMessageToUi;

    private void cityIsFavorite(boolean isFavorite){
        cachedCity.isFavorite = isFavorite;
    }

    public void resumePresenter(Bundle saveDataBundle) {
        cachedCity = (City) saveDataBundle.get(CITY_VIEW_MODEL);
        if (cachedCity != null) {
            TempCity.getInstance().setModelCityWeather(
                    (ModelCityWeather) saveDataBundle.get(CITY_TEMP_DB_MODEL));
        }
        showInformation(cachedCity);
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
                    stopAnimation();
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
                    showInformation(cachedCity);
                    stopAnimation();
                }
            });
        }

        @Override
        public void onErrorConnect() {
            sendMessageToUi.post(new Runnable() {
                @Override
                public void run() {
                    activity.showMessage(R.string.str_error_connect_to_internet);
                    activity.getBinding().setIsLoading(false);
                    stopAnimation();
                }
            });
        }

        @Override
        public void isEmpty() {
            sendMessageToUi.post(new Runnable() {
                @Override
                public void run() {
                    isLoading = false;
                    showInformation(null);
                    activity.getBinding().setIsLoading(false);
                    stopAnimation();
                }
            });
        }
    };

    public void clearInputText() {
        activity.clearInputText();
    }

    private void cachedInfo(City city) {
        isLoading = false;
        cachedCity = city;
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

        App.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                new CitiesDataRepository().getCity(fullCityName, cityCallback);
            }
        });
    }

    private void addToFavorite() {
        new CitiesDataRepository().add(
                TempCity.getInstance().getModelCityWeather());
        cityIsFavorite(true);
        showMessage(R.string.activity_favorite_add_to_favorite);
    }

    private void deleteFromFavorite() {
        new CitiesDataRepository().delete(
                TempCity.getInstance().getModelCityWeather());
        cityIsFavorite(false);
        showMessage(R.string.activity_favorite_del_from_favorite);
    }

    public void checkEndTask() {
        if (isLoading) {
            activity.getBinding().setIsLoading(true);
        } else {
//            hideViewLoading();
//            if (cachedCity != null) {
            showInformation(cachedCity);
//            }
        }
    }

    public void showInformation(City city) {
        activity.getBinding().setCity(city);
        activity.getBinding().setIsLoading(false);
        if (city != null) {
            activity.refreshWeathers(cachedCity.getWeathers());
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
