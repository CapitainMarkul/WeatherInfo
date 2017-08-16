package com.tensor.dapavlov1.tensorfirststep.presentation.activity.favorite.presenter;

import android.app.Activity;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.favorite.view.activity.FavoriteActivity;
import com.tensor.dapavlov1.tensorfirststep.provider.Callback;
import com.tensor.dapavlov1.tensorfirststep.interfaces.Router;
import com.tensor.dapavlov1.tensorfirststep.provider.common.CheckConnect;
import com.tensor.dapavlov1.tensorfirststep.provider.DataProvider;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.R;

import java.io.IOException;
import java.util.List;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class FavoritePresenter  {

    private FavoriteActivity activity;
    public FavoritePresenter(FavoriteActivity activity){
        this.activity = activity;
    }

    private boolean isOnline() {
        return CheckConnect.getInstance().isOnline(App.getContext());
    }

    public void showFavoriteCard() {
        showViewLoading();
        DataProvider.getInstance().getCitiesFromBd(new Callback<List<City>>() {
            //Читаем из БД
            @Override
            public void onSuccess(List<City> result) {
                //Проверяем результат чтения
                if (result != null && result.size() > 0) {
                    if (isOnline()) {
                        try {
                            //Обновляем информацию о погоде
                            DataProvider.getInstance().updateCityInfo(
                                    new Callback<List<City>>() {
                                        //обновляем погоду
                                        @Override
                                        public void onSuccess(List<City> result) {
                                            hideViewLoading();
                                            refreshWeathers(result);
                                        }
                                    }, result);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //Показываем устаревшую информацию из БД
                        hideViewLoading();
                        showMessage(R.string.str_error_connect_to_internet);
//                                App.getContext().getResources().getString(
//                                        R.string.str_error_connect_to_internet));
                        refreshWeathers(result);
                    }
                } else {
                    //если БД пуста, то показываем карточку с подсказкой!
                    showNothingCities();
                }
            }
        });
    }

    private Router router;

    public void setRouter(Router router){
        this.router = router;
    }

    public void changeActivity(Activity thisActivity, Class toActivity){
        router.goToNewActivity(thisActivity, toActivity);
    }

    public void deleteCity(int position) {
        DataProvider.getInstance().deleteCity(position);
    }

    private void showNothingCities() {
        activity.showEmptyCard();
    }

    private void showMessage(int message) {
        activity.showMessage(message);
    }

    private void hideViewLoading() {
        activity.hideLoading();
    }

    private void showViewLoading() {
        activity.showLoading();
    }

    private void refreshWeathers(List<City> cities) {
        activity.refreshWeathers(cities);
    }
}