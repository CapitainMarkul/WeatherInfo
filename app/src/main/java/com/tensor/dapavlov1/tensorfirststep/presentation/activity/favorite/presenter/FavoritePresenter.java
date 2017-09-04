package com.tensor.dapavlov1.tensorfirststep.presentation.activity.favorite.presenter;

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

import io.reactivex.android.schedulers.AndroidSchedulers;


/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public class FavoritePresenter extends BasePresenter<FavoriteActivity> {
    private Router router;

    private List<City> cachedCities = new ArrayList<>();
    private boolean isLoading = false;

    private CallbackCities<City> callbackCities = new CallbackCities<City>() {
        @Override
        public void onUpdate(final City result) {
            try {
                if (activity != null) {
                    showCity(result);
                    cachedInfo(result);
                    activity.getBinding().setIsLoading(false);
                }
            } catch (NullPointerException e) {
                Toast.makeText(App.getContext(), App.getContext().getText(R.string.unknown_error), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onOldFromDb(final City result) {
//            sendMessageToUi.post(() -> {
//            cachedInfo(result);
//            showCity(result);
//                showCachedCities();

//                activity.getBinding().setCities(result);
//                activity.getBinding().setIsLoading(false);

            activity.showMessage(R.string.str_error_connect_to_internet);
//            });
        }

        @Override
        public void isEmpty() {
//            sendMessageToUi.post(() -> {
            isLoading = true;
            activity.getBinding().setCities(null);
            activity.getBinding().setCity(null);
//                activity.getBinding().setIsLoading(false);
//            });
        }

        @Override
        public void onComplete() {
            activity.getBinding().setIsLoading(false);
            activity.showMessage(R.string.activity_favorite_update_success);
        }
    };

    private void cachedInfo(City city) {
        isLoading = true;
        cachedCities.add(city);
    }

    public void updateWeathers() {
        isLoading = false;
        cachedCities.clear();
        activity.getBinding().setIsLoading(true);

        new CitiesDataRepository().getCitiesRx()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        city -> callbackCities.onUpdate(city),
                        throwable -> callbackCities.onComplete(),
                        () -> callbackCities.onComplete());
    }

    public void switchActivity() {
        router.goToNewActivity(activity, AddCityActivity.class);
    }

    public void deleteCity(int position) {
        new CitiesDataRepository().delete(position);
    }

    private void showCity(City city) {
        activity.setItemInAdapter(city);
        activity.getBinding().setCity(city);
    }

    public void clearCacheCities(){
        cachedCities.clear();
    }

    public void showCachedCities() {
        if (cachedCities != null && !cachedCities.isEmpty()) {
            activity.setItemsInAdapter(cachedCities);
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