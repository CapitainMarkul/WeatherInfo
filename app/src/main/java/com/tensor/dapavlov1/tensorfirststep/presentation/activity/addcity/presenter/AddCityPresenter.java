package com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.tensor.dapavlov1.tensorfirststep.Callback;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.TempCity;
import com.tensor.dapavlov1.tensorfirststep.provider.DataProvider;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */

@InjectViewState
public class AddCityPresenter extends MvpPresenter<com.tensor.dapavlov1.tensorfirststep.interfaces.AddCityPresenter> {
    //для возможности добавления в БД
    //переведено в SingleTone (TempCity)
//    private ModelCityWeather modelCityWeather;

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
////            Map<String, String> map = ApiFabrica.getInstance().createMapForWeatherApi(objects[0].toString(),
////                    BuildConfig.WEATHER_API_KEY,
////                    BuildConfig.WEATHER_API_LANGUAGE,
////                    BuildConfig.WEATHER_API_COUNT_DAY);
//            try {
////                gsonCity = GsonFactory.getInstance().createGsonCityModel(
////                        ApiFabrica.getInstance().getJsonFromApiWeather(map)
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
        DataProvider.getInstance().deleteCityFromDataBase(
                TempCity.getInstance().getModelCityWeather());
        showMessage(R.string.activity_favorite_del_from_favorite);
    }

    private void hideViewLoading() {
        getViewState().hideLoading();
    }

    private void showViewLoading() {
        getViewState().showLoading();
    }

    private void showCardWeatherInfo() {
        getViewState().showWeatherCardFullInfo();
    }

    private void showCardEmpty() {
        getViewState().showWeatherCardNothingFind();
    }

    private void showInformation(City city) {
        getViewState().showInformation(city);
    }

    private void showMessage(int message) {
        getViewState().showMessage(message);
    }

    private void showCityIsFavorite(Boolean checked) {
        getViewState().setChecked(checked);
    }
}
