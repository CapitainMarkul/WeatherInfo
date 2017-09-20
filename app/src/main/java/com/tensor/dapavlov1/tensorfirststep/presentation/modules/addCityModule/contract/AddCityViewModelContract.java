package com.tensor.dapavlov1.tensorfirststep.presentation.modules.addCityModule.contract;

import android.databinding.Bindable;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;
import com.tensor.dapavlov1.tensorfirststep.presentation.common.visual.SwitchGradient;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.presenter.MvpPresenter;
import com.tensor.dapavlov1.tensorfirststep.presentation.modules.architecture.viewmodel.MvpViewModel;

/**
 * Created by da.pavlov1 on 20.09.2017.
 */

public interface AddCityViewModelContract {
    interface ViewModel extends MvpViewModel {

        @Bindable
        CityView getCity();
        void setCityView(CityView city);

        @Bindable
        boolean isLoading();
        void setLoading(boolean isLoading);

        @Bindable
        boolean isFirstLaunch();
        void setFirstLaunch(boolean isFirstLaunch);

        @Bindable
        boolean isFavorite();
        void setFavorite(boolean isFavorite);

        @Bindable
        boolean isAnimation();

        @Bindable
        SwitchGradient getSwitchGradient();

        void clearInputText();
        void onItemClick();
    }

    //Интерфейс для взаимодействия с презентором из Активити
    interface Presenter extends MvpPresenter<ViewModel> {
        void getWeatherInCity(String inputText);
        void onFavoriteClick();
    }
}
