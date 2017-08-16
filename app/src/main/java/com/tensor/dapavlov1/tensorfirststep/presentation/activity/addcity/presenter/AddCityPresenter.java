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

    public AddCityPresenter(AddCityActivity addCityActivity){
        this.addCityActivity = addCityActivity;
    }

    public void getWeatherInCity(String fullCityName) {
        showViewLoading();

        DataProvider.getInstance().getWeathers(new Callback<City>() {
            @Override
            public void onSuccess(City resultCity, boolean isFavorite) {
                if (resultCity != null) {
                    showCityIsFavorite(isFavorite);

                    //Показываем результат пользователю
                    showCardWeatherInfo();
                    showInformation(resultCity);
                }
//                else {
//                    //город не найден
//                    showCardEmpty();
//                }
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

//    private class AsyncResponseWeather extends AsyncTask {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            getViewState().showLoading();
//        }
//
//        @Override
//        protected Object doInBackground(Object[] objects) {
////            Map<String, String> map = ApiFabric.getInstance().createMapForWeatherApi(objects[0].toString(),
////                    BuildConfig.WEATHER_API_KEY,
////                    BuildConfig.WEATHER_API_LANGUAGE,
////                    BuildConfig.WEATHER_API_COUNT_DAY);
//            try {
////                gsonCity = GsonFactory.getInstance().createGsonCityModel(
////                        ApiFabric.getInstance().getJsonFromApiWeather(map)
////                );
//                //Test
//                modelCityWeather = MapperGsonToDb.getInstance().convertGsonModelToDaoModel(gsonCity);
//                //End Test
////                return MapperGsonToView.getInstance().convertGsonModelToViewModel(gsonCity);
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Object findCities) {
//            super.onPostExecute(findCities);
////
////            if (findCities != null) {
////                getViewState().showWeatherCardFullInfo();
////                getViewState().showInformation((City) findCities);
////            } else {
////                getViewState().showWeatherCardNothingFind();
////            }
////            getViewState().hideLoading();
////        }
//        }

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
