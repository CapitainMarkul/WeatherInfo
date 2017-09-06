package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.CityView;

import java.util.List;

/**
 * Created by da.pavlov1 on 23.08.2017.
 */

public class NetworkConnectException extends Exception {
    private List<CityView> oldCitiesInfo;

    public NetworkConnectException(List<CityView> oldCitiesInfo) {
        this.oldCitiesInfo = oldCitiesInfo;
    }

    public NetworkConnectException() {
    }

    public List<CityView> getOldCitiesInfo() {
        return oldCitiesInfo;
    }
}
