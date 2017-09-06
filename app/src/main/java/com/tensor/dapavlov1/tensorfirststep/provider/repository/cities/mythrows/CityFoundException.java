package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;

/**
 * Created by da.pavlov1 on 23.08.2017.
 */

public class CityFoundException extends Exception {
    private CityView cityView;

    public CityFoundException(CityView cityView) {
        this.cityView = cityView;
    }

    public CityView getCityView() {
        return cityView;
    }
}
