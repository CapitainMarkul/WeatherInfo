package com.tensor.dapavlov1.tensorfirststep.presentation.activity.favorite.presenter;

import android.app.Activity;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.PresenterCallBack;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.favorite.view.activity.FavoriteActivity;
import com.tensor.dapavlov1.tensorfirststep.provider.Callback;
import com.tensor.dapavlov1.tensorfirststep.interfaces.Router;
import com.tensor.dapavlov1.tensorfirststep.provider.common.CheckConnect;
import com.tensor.dapavlov1.tensorfirststep.provider.DataProvider;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class FavoritePresenter {

    private FavoriteActivity activity;

    private Router router;

    private List<City> cachedCities = new ArrayList<>();
    private boolean isRefresh = false;

    ReentrantLock lock = new ReentrantLock();
    private PresenterCallBack presenterCallBack = new PresenterCallBack() {
        @Override
        public void onSuccess() {
//            standByActivity();
            lock.lock();try {
                activity.setRefreshLayout(getRefresh());
                activity.refreshWeathers(getCachedCities());
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void onNothingFind() {
//            standByActivity();
            lock.lock();try {
                activity.setRefreshLayout(getRefresh());
                activity.showEmptyCard();
            } finally {
                lock.unlock();
            }
        }
    };

    private void cachedInfo(List<City> cities) {
        isRefresh = false;

        cachedCities.clear();
        cachedCities.addAll(cities);
    }

    public boolean getRefresh() {
        return isRefresh;
    }

    public List<City> getCachedCities() {
        return cachedCities;
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    public void setActivity(FavoriteActivity activity) {
        this.activity = activity;
    }

    private boolean isOnline() {
        return CheckConnect.getInstance().isOnline(App.getContext());
    }

    public void updateWeathers() {
        isRefresh = true;
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
                                            //кешируем результат
                                            cachedInfo(result);
                                            presenterCallBack.onSuccess();
                                        }
                                    }, result);
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
                    cachedInfo(null);
                    presenterCallBack.onNothingFind();
                }
            }
        });
    }


//    public boolean compareList(List<City> oldCities) {
//        if (cachedCities != null) {
//            if (oldCities.size() != cachedCities.size()) {
//                return true;
//            } else if (oldCities.containsAll(cachedCities) && cachedCities.containsAll(oldCities)) {
//                return false;
//            }
//            return true;
//        }
//        return false;
//    }

//    private void standByActivity() {
//        while (this.activity == null) {
//            try {
//                Thread.sleep(200);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public void changeActivity(Activity thisActivity, Class toActivity) {
        router.goToNewActivity(thisActivity, toActivity);
    }

    public void deleteCity(int position) {
        DataProvider.getInstance().deleteCity(position);
    }
}