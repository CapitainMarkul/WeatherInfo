package com.tensor.dapavlov1.tensorfirststep.presentation.activity.favorite.presenter;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.tensor.dapavlov1.tensorfirststep.App;
import com.tensor.dapavlov1.tensorfirststep.presentation.activity.addcity.view.activity.AddCityActivity;
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
    private boolean isLoading = false;

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
                            activity.getBinding().setIsLoading(false);

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
                    activity.getBinding().setCities(result);
                    activity.getBinding().setIsLoading(false);

                    activity.showMessage(R.string.str_error_connect_to_internet);
                }
            });
        }

        @Override
        public void isEmpty() {
            sendMessageToUi.post(new Runnable() {
                @Override
                public void run() {
                    isLoading = true;
                    activity.getBinding().setCities(null);
                    activity.getBinding().setIsLoading(false);
                }
            });
        }
    };

    private void cachedInfo(List<City> cities) {
        isLoading = true;

        cachedCities.clear();
        cachedCities.addAll(cities);
    }

    public void updateWeathers() {
        isLoading = false;
        activity.getBinding().setIsLoading(true);

        App.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                new CitiesDataRepository().getCities(callbackCities);
            }
        });
    }

    public void switchActivity() {
        router.goToNewActivity(activity, AddCityActivity.class);
    }

    public void deleteCity(int position) {
        new CitiesDataRepository().delete(position);
    }

    public void showCachedCities() {
        if (cachedCities != null && !cachedCities.isEmpty()) {
            activity.refreshAdapter(cachedCities);
        }
        activity.getBinding().setCities(cachedCities);
    }

    public boolean getLoading() {
        return isLoading;
    }

    public void setRouter(Router router) {
        this.router = router;
    }
}