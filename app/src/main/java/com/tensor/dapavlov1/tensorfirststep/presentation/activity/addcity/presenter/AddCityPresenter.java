package com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.presenter;

import com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.view.activity.AddCityActivity;
import com.tensor.dapavlov1.tensorfirststep.provider.Callback;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.TempCity;
import com.tensor.dapavlov1.tensorfirststep.provider.DataProvider;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */


public class AddCityPresenter {
    //для возможности добавления в БД
    //переведено в SingleTone (TempCity)
//    private ModelCityWeather modelCityWeather;
    private AddCityActivity addCityActivity;

    public void setActivity(AddCityActivity addCityActivity) {
        this.addCityActivity = addCityActivity;
    }

    public void getWeatherInCity(String fullCityName) {
        showViewLoading();

        DataProvider.getInstance().getWeathers(new Callback<City>() {
            @Override
            public void onSuccess(City resultCity, boolean isFavorite) {
                if (resultCity != null) {
                    showCityIsFavorite(isFavorite);
//
//                    try {
//                        Thread.sleep(3000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }

                    //Показываем результат пользователю
                    showCardWeatherInfo();
                    addCityActivity.setCity(resultCity);
                    showInformation(resultCity);
                }
                hideViewLoading();
            }

            @Override
            public void onFail() {
                //город не найден
                showCardEmpty();
                hideViewLoading();
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
        addCityActivity.hideLoading();
    }

    private void showViewLoading() {
        addCityActivity.showLoading();
    }

    private void showCardWeatherInfo() {
        addCityActivity.showWeatherCardFullInfo();
    }

    private void showCardEmpty() {
        addCityActivity.showWeatherCardNothingFind();
    }

    private void showInformation(City city) {
        addCityActivity.showInformation(city);
    }

    private void showMessage(int message) {
        addCityActivity.showMessage(message);
    }

    private void showCityIsFavorite(Boolean checked) {
        addCityActivity.setChecked(checked);
    }
}
