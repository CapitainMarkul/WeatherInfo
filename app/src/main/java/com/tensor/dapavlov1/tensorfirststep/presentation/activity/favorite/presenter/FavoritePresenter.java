package com.tensor.dapavlov1.tensorfirststep.presentation.activity.favorite.presenter;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.BasePresenter;
import com.tensor.dapavlov1.tensorfirststep.R;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.favorite.view.activity.FavoriteActivity;
import com.tensor.dapavlov1.tensorfirststep.interfaces.Router;
import com.tensor.dapavlov1.tensorfirststep.provider.callbacks.CallbackCities;
import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;
import com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.CitiesDataRepository;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class FavoritePresenter extends BasePresenter<FavoriteActivity> {
    private Router router;

    private List<City> cachedCities = new ArrayList<>();
    private boolean isRefreshComplete = false;

    //для связи с UI
    private Handler sendMessageToUi;

    public FavoritePresenter() {
        sendMessageToUi = new Handler(Looper.getMainLooper());
    }

    private CallbackCities<List<City>> callbackCities = new CallbackCities<List<City>>() {
        @Override
        public void onUpdate(final List<City> result) {
            try {
                if (activity != null) {
                    sendMessageToUi.post(new Runnable() {
                        @Override
                        public void run() {
                            cachedInfo(result);
                            showCachedCities();

                            activity.hideEmptyCard();
                            activity.runRefreshLayout(false);

                            activity.showMessage(R.string.activity_favorite_update_success);
                        }
                    });
                }
            } catch (NullPointerException e) {
                Toast.makeText(App.getContext(), App.getContext().getText(R.string.activity_favorite_unknown_error), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onOldFromDb(final List<City> result) {
            sendMessageToUi.post(new Runnable() {
                @Override
                public void run() {
                    cachedInfo(result);
                    showCachedCities();

                    activity.hideEmptyCard();
                    activity.runRefreshLayout(false);

                    activity.showMessage(R.string.str_error_connect_to_internet);
                }
            });
        }

        @Override
        public void isEmpty() {
            sendMessageToUi.post(new Runnable() {
                @Override
                public void run() {
                    isRefreshComplete = true;
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

        App.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                new CitiesDataRepository().getCities(callbackCities);
            }
        });
    }

    public void switchActivity(Activity thisActivity, Class toActivity) {
        router.goToNewActivity(thisActivity, toActivity);
    }

    public void deleteCity(int position) {
        new CitiesDataRepository().delete(position);
//        DataProvider.getInstance().deleteCity();
    }

    public void showCachedCities() {
        if(cachedCities!= null && !cachedCities.isEmpty()) {
            activity.refreshWeathers(cachedCities);
        } else {
            activity.showEmptyCard();
        }
    }

    public boolean getRefreshComplete() {
        return isRefreshComplete;
    }

    public void setRouter(Router router) {
        this.router = router;
    }
}