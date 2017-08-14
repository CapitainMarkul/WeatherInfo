package com.tensor.dapavlov1.tensorfirststep.Presentation.Activity.FavoriteActivity.presenter;

import android.os.AsyncTask;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.BuildConfig;
import com.tensor.dapavlov1.tensorfirststep.CheckConnect;
import com.tensor.dapavlov1.tensorfirststep.GsonFactory;
import com.tensor.dapavlov1.tensorfirststep.Mappers.MapperGsonToDb;
import com.tensor.dapavlov1.tensorfirststep.data.DaoModels.DaoCity;
import com.tensor.dapavlov1.tensorfirststep.data.DaoModels.ModelCityWeather;
import com.tensor.dapavlov1.tensorfirststep.data.DaoModels.WorkWithDataBase;
import com.tensor.dapavlov1.tensorfirststep.Mappers.MapperDbToView;
import com.tensor.dapavlov1.tensorfirststep.data.ViewModels.City;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.interfaces.FavoritePresenter;
import com.tensor.dapavlov1.tensorfirststep.ConnectToApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */
@InjectViewState
public class FavoriteViewPresenter extends MvpPresenter<FavoritePresenter> {
    private List<DaoCity> daoCityList;

    public void showFavoriteCard() {
        //Начинать подгрузку из БД (проверить интернет, если он есть, то обновить)
        daoCityList = WorkWithDataBase.getInstance().loadListAllCity();

        if (daoCityList != null && daoCityList.size() > 0) {
            getViewState().hideEmptyCard();

            if (CheckConnect.getInstance().isOnline(App.getContext())) {
                new AsyncUpdateWeathers().execute();
            } else {
//                выводим старую информацию, без обновления. Показываем предупреждение об
//                отсутствии интернета
                getViewState().showMessage(
                        App.getContext().getResources().getString(R.string.str_error_connect_to_internet));
                getViewState().refreshWeathers(
                        MapperDbToView.getInstance().getCityViewModels(daoCityList)
                );
            }
        } else {
            //если БД пуста, то показываем карточку с подсказкой!
            getViewState().showEmptyCard();
        }
    }

    public void deleteCity(int position){
        WorkWithDataBase.getInstance().deleteCity(position);
    }

    private class AsyncUpdateWeathers extends AsyncTask<Object, Void, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showLoading
            getViewState().showLoading();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            List<ModelCityWeather> modelCityWeathers = new ArrayList<>();

            for (DaoCity item : daoCityList) {
                try {
                    modelCityWeathers.add(
                            MapperGsonToDb.getInstance().convertGsonModelToDaoModel(
                                    GsonFactory.getInstance().createGsonCityModel(
                                            ConnectToApi.getInstance().getJsonFromApiWeather(
                                                    ConnectToApi.getInstance().createMapForWeatherApi(
                                                            item.getName(),
                                                            BuildConfig.WEATHER_API_KEY,
                                                            BuildConfig.WEATHER_API_LANGUAGE,
                                                            BuildConfig.WEATHER_API_COUNT_DAY
                                                    )
                                            )
                                    )
                            )
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //set update Weathers
            WorkWithDataBase.getInstance().updateAllCity(modelCityWeathers);
            //return update Table from BD
            return MapperDbToView.getInstance().getCityViewModels(
                    WorkWithDataBase.getInstance().loadListAllCityForCurrentThread()
            );
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

//            daoCityList = query.list();
            getViewState().refreshWeathers((List<City>) result);

            getViewState().hideLoading();
            getViewState().showMessage(
                    App.getContext().getResources().getString(R.string.activity_favorite_update_success));
        }
    }
}
