package com.tensor.dapavlov1.tensorfirststep.presentation.activity.favorite.presenter;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.BasePresenter;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.PresenterCallBack;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.favorite.view.activity.FavoriteActivity;
import com.tensor.dapavlov1.tensorfirststep.provider.Callback;
import com.tensor.dapavlov1.tensorfirststep.interfaces.Router;
import com.tensor.dapavlov1.tensorfirststep.provider.common.CheckConnect;
import com.tensor.dapavlov1.tensorfirststep.provider.DataProvider;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class FavoritePresenter extends BasePresenter<FavoriteActivity> {
//    private FavoriteActivity activity;

    private Router router;

    private List<City> cachedCities = new ArrayList<>();
    private boolean isRefreshComplete = false;

    //для связи с UI
    private Handler sendMessageToUi;

    public FavoritePresenter() {
        sendMessageToUi = new Handler(Looper.getMainLooper());
    }

    private PresenterCallBack presenterCallBack = new PresenterCallBack() {
        @Override
        public void onSuccess() {
            try {
                if (activity != null) {
                    sendMessageToUi.post(new Runnable() {
                        @Override
                        public void run() {
                            activity.refreshWeathers(cachedCities);
                            activity.runRefreshLayout(false);
                        }
                    });
                }
            } catch (NullPointerException e) {
                Toast.makeText(App.getContext(), App.getContext().getText(R.string.activity_favorite_unknown_error), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onNothingFind() {
            sendMessageToUi.post(new Runnable() {
                @Override
                public void run() {
                    activity.runRefreshLayout(getRefreshComplete());
                    activity.showEmptyCard();
                }
            });
        }
    };


    private void cachedInfo(List<City> cities) {
        isRefreshComplete = true;

        cachedCities.clear();
        cachedCities.addAll(cities);
    }

    public void updateWeathers() {
        isRefreshComplete = false;
        activity.runRefreshLayout(true);

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
                                    result,
                                    new Callback<List<City>>() {
                                        //обновляем погоду
                                        @Override
                                        public void onSuccess(List<City> result) {
                                            //кешируем результат
                                            cachedInfo(result);
                                            presenterCallBack.onSuccess();
                                        }
                                    });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //кешируем результат
                        cachedInfo(result);
                        presenterCallBack.onSuccess();
                    }
                } else {
                    //если БД пуста, то показываем карточку с подсказкой!

                    isRefreshComplete = true;
                    presenterCallBack.onNothingFind();
                }
            }
        });
    }

    public void switchActivity(Activity thisActivity, Class toActivity) {
        router.goToNewActivity(thisActivity, toActivity);
    }

    public void deleteCity(int position) {
        DataProvider.getInstance().deleteCity(position);
    }

    public void showCachedCities() {
        activity.refreshWeathers(cachedCities);
    }

    public boolean getRefreshComplete() {
        return isRefreshComplete;
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    private boolean isOnline() {
        return CheckConnect.getInstance().isOnline(App.getContext());
    }
}