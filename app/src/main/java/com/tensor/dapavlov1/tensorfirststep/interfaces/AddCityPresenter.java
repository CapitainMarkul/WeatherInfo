package com.tensor.dapavlov1.tensorfirststep.interfaces;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;

/**
 * Created by da.pavlov1 on 03.08.2017.
 */

public interface AddCityPresenter extends Loading, ShowMessage {
    void showInformation(City city);

    void showWeatherCardNothingFind();

//    void showInformation(City getCities, String dateTimeNow);

    void showWeatherCardFullInfo();

    void cityIsFavorite(Boolean checked);
}
