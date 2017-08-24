package com.tensor.dapavlov1.tensorfirststep.provider.repository.cities.mythrows;

import com.tensor.dapavlov1.tensorfirststep.data.viewmodels.City;

import java.util.List;

/**
 * Created by da.pavlov1 on 23.08.2017.
 */

public class NetworkConnectException extends Exception {
    private List<City> oldCitiesInfo;

    public NetworkConnectException(List<City> oldCitiesInfo) {
        this.oldCitiesInfo = oldCitiesInfo;
    }

    public NetworkConnectException() {
    }

    public List<City> getOldCitiesInfo() {
        return oldCitiesInfo;
    }
}
