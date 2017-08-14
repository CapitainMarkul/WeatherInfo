package com.tensor.dapavlov1.tensorfirststep.interfaces;

import com.arellomobile.mvp.MvpView;
import com.tensor.dapavlov1.tensorfirststep.data.ViewModels.City;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public interface AddCityPresenter extends MvpView, Loading, ShowMessage {
    void showInformation(City city);

    void showWeatherCardNothingFind();

//    void showInformation(City cities, String dateTimeNow);

    void showWeatherCardFullInfo();
}
