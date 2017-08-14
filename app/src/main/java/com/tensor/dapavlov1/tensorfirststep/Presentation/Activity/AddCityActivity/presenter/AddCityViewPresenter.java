package com.tensor.dapavlov1.tensorfirststep.Presentation.Activity.AddCityActivity.presenter;

import android.os.AsyncTask;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.BuildConfig;
import com.tensor.dapavlov1.tensorfirststep.GsonFactory;
import com.tensor.dapavlov1.tensorfirststep.Mappers.MapperGsonToDb;
import com.tensor.dapavlov1.tensorfirststep.Mappers.MapperGsonToView;
import com.tensor.dapavlov1.tensorfirststep.data.DaoModels.DaoCity;
import com.tensor.dapavlov1.tensorfirststep.data.DaoModels.ModelCityWeather;
import com.tensor.dapavlov1.tensorfirststep.data.DaoModels.WorkWithDataBase;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.TrimCityInfo;
import com.tensor.dapavlov1.tensorfirststep.data.GsonModels.GsonCity;
import com.tensor.dapavlov1.tensorfirststep.data.ViewModels.City;
import com.tensor.dapavlov1.tensorfirststep.interfaces.AddCityPresenter;
import com.tensor.dapavlov1.tensorfirststep.ConnectToApi;

import java.io.IOException;
import java.util.Map;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */

@InjectViewState
public class AddCityViewPresenter extends MvpPresenter<AddCityPresenter> {
    private ModelCityWeather modelCityWeather;
    private GsonCity gsonCity;

    public void sendRequestGoogleApi(String fullCityName) {
        AsyncResponseWeather asyncResponseWeather = new AsyncResponseWeather();
        asyncResponseWeather.execute(
                TrimCityInfo.getInstance().trimCityName(fullCityName));
    }

    class AsyncResponseWeather extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getViewState().showLoading();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            Map<String, String> map = ConnectToApi.getInstance().createMapForWeatherApi(objects[0].toString(),
                    BuildConfig.WEATHER_API_KEY,
                    BuildConfig.WEATHER_API_LANGUAGE,
                    BuildConfig.WEATHER_API_COUNT_DAY);
            try {
                gsonCity = GsonFactory.getInstance().createGsonCityModel(
                        ConnectToApi.getInstance().getJsonFromApiWeather(map)
                );
                //TODO: можно ли сразу создать объект DB типа, для удобства работы? (Чтобы не мапить каждый раз объект типа GsonCity)
                //Test
                modelCityWeather = MapperGsonToDb.getInstance().convertGsonModelToDaoModel(gsonCity);
                //End Test
                return MapperGsonToView.getInstance().convertGsonModelToViewModel(gsonCity);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object findCities) {
            super.onPostExecute(findCities);

            if (findCities != null) {
                getViewState().showWeatherCardFullInfo();
                getViewState().showInformation((City) findCities);
            } else {
                getViewState().showWeatherCardNothingFind();
            }
            getViewState().hideLoading();
        }
    }

    public void addToFavorite() {
        WorkWithDataBase.getInstance().setInDataBase(
                modelCityWeather.getDaoCity(),
                modelCityWeather.getWeathers());
        getViewState().showMessage(
                App.getContext().getResources().getString(R.string.activity_favorite_add_to_favorite));
    }

    // проверка на наличие города в БД
    public boolean isFavorite() {
        DaoCity tempCity = WorkWithDataBase.getInstance().isAdd(modelCityWeather.getDaoCity());
        if (tempCity != null) {
            //меняем ссылку на объект (если мы захотим убрать его из избранного, прямо с этого экрана)
            modelCityWeather = new ModelCityWeather(tempCity, tempCity.getWeathers());
            return true;
        }
        return false;
    }

    public void deleteFromFavorite() {
        WorkWithDataBase.getInstance().deleteCity(
                modelCityWeather.getDaoCity());
        getViewState().showMessage(
                App.getContext().getResources().getString(R.string.activity_favorite_del_from_favorite));
    }
    //For debug
//    private void listWeathers() {
//        List<DaoCity> cities = App.getDaoSession().getModelCityDao().loadAll();
//        List<String> weathers = new ArrayList<>();
//
//        for (DaoCity item : cities) {
//            for (DaoWeather itemW : item.getWeathers()) {
//                weathers.add(String.valueOf(itemW.getCityId()));
//            }
//            String cityWeather = TextUtils.join(",", weathers);
//            weathers.clear();
//            Log.e("Result:", String.format("%s (%s)", item.getName(), cityWeather));
//        }
//    }
}
