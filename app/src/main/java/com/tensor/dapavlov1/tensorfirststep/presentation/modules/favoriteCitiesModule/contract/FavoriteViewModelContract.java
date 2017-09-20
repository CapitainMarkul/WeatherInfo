package com.tensor.dapavlov1.tensorfirststep.presentation.modules.favoriteCitiesModule.contract;

import android.databinding.Bindable;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.presenter.MvpPresenter;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.viewmodel.MvpViewModel;

import java.util.List;

/**
 * Created by da.pavlov1 on 20.09.2017.
 */

public interface FavoriteViewModelContract {
    interface ViewModel extends MvpViewModel {

        @Bindable
        List<CityView> getCitiesView();
        void addCityView(CityView city);
        void setCitiesView(List<CityView> citiesView);
        void delCityView(CityView city);
        CityView getLastCity();

        @Bindable
        CityView getLastDeletedCity();

        @Bindable
        boolean isLoading();
        void setLoading(boolean isLoading);

        @Bindable
        boolean isResetAdapter();
        void setResetAdapter(boolean reset);
        void resetAdapter();

        void showEmptyResult();

    }

    interface Presenter extends MvpPresenter<ViewModel> {
        void deleteCity(CityView city);
        void updateWeathers();
        void switchActivity();
    }
}
